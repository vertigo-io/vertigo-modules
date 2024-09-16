package io.vertigo.easyforms.impl.designer;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HtmlSanatizerUtil {

	private static final PolicyFactory SANATIZER = Sanitizers.FORMATTING
			.and(Sanitizers.BLOCKS)
			.and(Sanitizers.LINKS)
			.and(Sanitizers.STYLES)
			.and(Sanitizers.IMAGES)
			.and(Sanitizers.TABLES)
			.and(new HtmlPolicyBuilder()
					.allowElements("font")
					.allowAttributes("size", "face", "color").onElements("font") // quasar wysiwyg editor still uses font tag
					.allowElements( // force target _blank https://github.com/OWASP/java-html-sanitizer/issues/147
							(elementName, attrs) -> {
								final int targetIndex = attrs.indexOf("target");
								if (targetIndex < 0) {
									attrs.add("target");
									attrs.add("_blank");
								} else {
									attrs.set(targetIndex + 1, "_blank");
								}
								return elementName;
							},
							"a")
					.toFactory());

	public static String sanatizeHtml(final String in) {
		return SANATIZER.sanitize(in);
	}
}
