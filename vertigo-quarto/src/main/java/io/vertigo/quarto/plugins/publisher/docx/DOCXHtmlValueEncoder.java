/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.quarto.plugins.publisher.docx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vertigo.commons.codec.Encoder;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Tuple;
import io.vertigo.core.util.StringUtil;

/**
 * Implentation encoding HTML data to DOCX.
 * Support simple html richText :
 * Tags :
 * - html : html value must be in a html tag
 * - p : paragraphe
 * - text-align: center | left | right : for alignment
 * - u : underline
 * - b : bold
 * - i : italic
 *
 * @author npiedeloup
 */
final class DOCXHtmlValueEncoder implements Encoder<String, String> {

	private static final Pattern TEXT_ALIGN_PATTERN = Pattern.compile("text-align\\s*:\\s*(\\w+)\\s*;");

	/** {@inheritDoc} */
	@Override
	public String encode(final String toEncode) {
		if (toEncode == null) {
			return null;
		}
		Assertion.check().isTrue(toEncode.startsWith("<html>") && toEncode.endsWith("</html>"), "This encoded must be use for html content only");
		//---

		final var parsedXhtml = parseXHtml(toEncode);
		Assertion.check().isTrue("html".equals(parsedXhtml.tagName), "RichText must start with an unique root <html> tag");
		//---
		final var encoded = new StringBuilder();
		encoded.append("<w:p>");
		final RichTextProperties currentProperties = new RichTextProperties(Map.of(), Map.of(), null, true);
		encodeXHtml(parsedXhtml, currentProperties, encoded, currentProperties);
		encoded.append("</w:p>");
		//System.out.println("=====>>>>>>>>>>>>>");
		//		System.out.println(toEncode);
		//		System.out.println("==================");
		//System.out.println(parsedXhtml);
		//System.out.println("==================");
		//System.out.println(encoded.toString().replace("</w:p>", "</w:p>\n"));
		//System.out.println("=====<<<<<<<<<<<<<");
		return encoded.toString();
	}

	/**
	 * Record to keep track of RichText properties.
	 * Use during rendering xhtml nodes to Docx.
	 */
	record RichTextProperties(Map<String, String> pPr, Map<String, String> rPr, String content, boolean root) {

		private static int forceOpenPCounter = 0;

		RichTextProperties(final Map<String, String> pPr, final Map<String, String> rPr, final String content, final boolean root, final boolean forceOpenP) {
			this(pPr, rPr, content, root);
			if (forceOpenP) {
				pPr.put("forceOpenP", String.valueOf(forceOpenPCounter++));
				rPr.put("forceOpenP", String.valueOf(forceOpenPCounter++));
				forceOpenPCounter = forceOpenPCounter % 100_000; //on ne veux pas de nombres trop grand
			}
		}

	}

	/**
	 * XHtml Node from parsing.
	 */
	record XHtmlNode(String tagName, String tagAttributes, String content, List<XHtmlNode> innerNodes) {

		XHtmlNode(final String content) {
			this(null, null, content, List.of());
		}

		XHtmlNode(final String tagName, final String tagAttributes, final List<XHtmlNode> innerNodes) {
			this(tagName, tagAttributes, null, innerNodes);
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			if (content != null) {
				builder.append(content);
			} else {
				builder.append("<").append(tagName);
				if (!tagAttributes.isEmpty()) {
					builder.append(tagAttributes);
				}
				builder.append(">");
				for (final XHtmlNode innerNode : innerNodes) {
					builder.append(innerNode);
				}
				builder.append("</").append(tagName).append(">");
			}
			return builder.toString();
		}

	}

	private XHtmlNode parseXHtml(final String xhtml) {
		//On ne veux que deux cas : soit une balise, soit du contenu
		final int nextTag = xhtml.indexOf("<");
		if (nextTag >= 0) {
			final var result = parseXHtmlTag(xhtml, 0);
			Assertion.check().isTrue(result.val1() == xhtml.length(), "XHtml parser need an unique root tag");
			return result.val2();
		} else {
			return new XHtmlNode(xhtml);
		}
	}

	private Tuple<Integer, XHtmlNode> parseXHtmlTag(final String xhtml, final int fromIndex) {
		final var startTagClosePos = xhtml.indexOf(">", fromIndex);
		final var startTagAttrPos = xhtml.indexOf(" ", fromIndex);
		final var startTagAutoClosePos = xhtml.indexOf("/>", fromIndex);
		final var isAutoClosedTag = startTagAutoClosePos == startTagClosePos - 1;
		final var contentIndex = startTagClosePos + 1;
		final var startTagNameEnd = startTagAttrPos > 0
				? Math.min(startTagClosePos, isAutoClosedTag ? startTagAutoClosePos : startTagAttrPos)
				: isAutoClosedTag ? startTagAutoClosePos : startTagAttrPos;
		final var tagName = xhtml.substring(fromIndex + 1, startTagNameEnd);
		final var tagAttributes = xhtml.substring(startTagNameEnd, isAutoClosedTag ? startTagAutoClosePos : startTagClosePos);
		final List<XHtmlNode> innerNodes = new ArrayList<>();
		//si autoClosed tag
		if (isAutoClosedTag) {
			return Tuple.of(startTagAutoClosePos + 2, new XHtmlNode(tagName, tagAttributes, innerNodes));
		}

		//si tag avec body
		int previousEndTag = contentIndex;
		int nextTag = xhtml.indexOf("<", previousEndTag);
		while (nextTag >= 0) {
			final var nextContent = xhtml.substring(previousEndTag, nextTag);
			if (!nextContent.isEmpty()) {
				innerNodes.add(new XHtmlNode(nextContent));
			}
			if (xhtml.indexOf("</" + tagName + ">", nextTag) == nextTag) { //si fin de tag
				return Tuple.of(nextTag + tagName.length() + 3, new XHtmlNode(tagName, tagAttributes, innerNodes)); //cas de sortie normale
			}
			Assertion.check().isTrue(xhtml.indexOf("</", nextTag) != nextTag, "close other tag found before close tag {0}", tagName);

			//encode tag
			final var innerNode = parseXHtmlTag(xhtml, nextTag);
			previousEndTag = innerNode.val1();
			innerNodes.add(innerNode.val2());
			nextTag = xhtml.indexOf("<", previousEndTag);
		}
		//end : pas de tag de fin trouvé
		throw new IllegalArgumentException("Missing close tag " + tagName);
	}

	private RichTextProperties encodeXHtml(final XHtmlNode xHtmlNode, final RichTextProperties outerProperties, final StringBuilder output, final RichTextProperties openedProperties) {
		if (xHtmlNode.tagName() == null && xHtmlNode.content() != null) {
			//content only : use current RichTextProperties
			output.append(encodedStartTag(outerProperties, openedProperties));
			output.append(encodeContentForXHtml(xHtmlNode.content()));
			output.append(encodedEndTag());
			return outerProperties;
		} else if (xHtmlNode.tagName() != null) {
			final var newProperties = alterRichTextProperties(xHtmlNode.tagName(), xHtmlNode.tagAttributes(), outerProperties);
			var newOpenedProperties = openedProperties;
			if (newProperties.content != null) {
				output.append(newProperties.content());
			}
			for (final var innerNode : xHtmlNode.innerNodes()) {
				newOpenedProperties = encodeXHtml(innerNode, newProperties, output, newOpenedProperties);
			}
			return newOpenedProperties;
		}
		return openedProperties;
	}

	private String encodeContentForXHtml(final String content) {
		final StringBuilder result = new StringBuilder(content);
		StringUtil.replace(result, "<", "&lt;"); //protect against html tags
		StringUtil.replace(result, ">", "&gt;");
		StringUtil.replace(result, String.valueOf((char) 128), String.valueOf((char) 8364));

		String strResult = result.toString();
		strResult = strResult.replaceAll("[\t\r\n]", " "); // in html, \r, \n and \t are not significant
		return strResult.replaceAll("[\s]+", " "); // keep only one space, because we use xml:spaces="preserve"
	}

	private RichTextProperties alterRichTextProperties(final String tagName, final String tagAttributes, final RichTextProperties outerProperties) {
		var outerPPr = outerProperties.pPr;
		var outerRPr = outerProperties.rPr;
		var forceOpenP = false;
		String content = null;
		switch (tagName) {
			case "html":
			case "inline":
				break;
			case "p":
				forceOpenP = true;
				outerPPr = new HashMap<>(outerPPr);
				outerRPr = new HashMap<>(outerRPr);
				outerPPr.put("spacing", "0");
				outerPPr.put("ind", "0");
				outerPPr.put("jc", "left");
				final Matcher matcher = TEXT_ALIGN_PATTERN.matcher(tagAttributes);
				if (matcher.find()) {
					outerPPr.put("jc", matcher.group(1));
				}
				break;
			case "b":
				outerRPr = new HashMap<>(outerRPr);
				outerRPr.put("b", "true");
				break;
			case "i":
				outerRPr = new HashMap<>(outerRPr);
				outerRPr.put("i", "true");
				break;
			case "u":
				outerRPr = new HashMap<>(outerRPr);
				outerRPr.put("u", "true");
				break;
			case "h5":
				outerPPr = new HashMap<>(outerPPr);
				forceOpenP = true;
				outerPPr.put("spacing", "300");
				outerRPr = new HashMap<>(outerRPr);
				outerRPr.put("sz", "36");
				break;
			case "h6":
				outerPPr = new HashMap<>(outerPPr);
				forceOpenP = true;
				outerPPr.put("spacing", "200");
				outerRPr = new HashMap<>(outerRPr);
				outerRPr.put("sz", "30");
				break;
			case "br":
				content = "<w:r><w:t><w:br/></w:t></w:r>";
				break;
		}

		return new RichTextProperties(outerPPr, outerRPr, content, false, forceOpenP);
	}

	private String encodedStartTag(final RichTextProperties currentProperties, final RichTextProperties openedProperties) {
		final StringBuilder result = new StringBuilder();
		if (!currentProperties.pPr.equals(openedProperties.pPr)) {
			if (!openedProperties.root) {
				result.append("</w:p>");
			}
			result.append("<w:p><w:pPr>");
			if (currentProperties.pPr.containsKey("spacing")) {
				result.append("<w:spacing w:before=\"").append(currentProperties.pPr.get("spacing")).append("\" w:after=\"").append(currentProperties.pPr.get("spacing")).append("\"/>");
			}
			if (currentProperties.pPr.containsKey("ind")) {
				result.append("<w:ind w:left=\"").append(currentProperties.pPr.get("ind")).append("\"/>");
			}
			if (currentProperties.pPr.containsKey("jc")) {
				result.append("<w:jc w:val=\"").append(currentProperties.pPr.get("jc")).append("\"/>");
			}
			result.append("</w:pPr>");
		}
		result.append("<w:r>");
		if (!currentProperties.rPr.equals(openedProperties.rPr)) {
			result.append("<w:rPr>");
			if (currentProperties.rPr.containsKey("b")) {
				result.append("<w:b/>");
			}
			if (currentProperties.rPr.containsKey("i")) {
				result.append("<w:i/>");
			}
			if (currentProperties.rPr.containsKey("u")) {
				result.append("<w:u w:val=\"single\"/>");
			}
			if (currentProperties.rPr.containsKey("sz")) {
				result.append("<w:sz w:val=\"").append(currentProperties.rPr.get("sz")).append("\"/>");
			}
			result.append("</w:rPr>");
		}
		result.append("<w:t xml:space=\"preserve\">");
		return result.toString();
	}

	private Object encodedEndTag() {
		return "</w:t></w:r>";
	}

}
