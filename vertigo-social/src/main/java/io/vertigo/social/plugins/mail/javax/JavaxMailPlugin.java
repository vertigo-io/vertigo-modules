/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2023, Vertigo.io, team@vertigo.io
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
package io.vertigo.social.plugins.mail.javax;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;

import io.vertigo.connectors.mail.MailSessionConnector;
import io.vertigo.core.analytics.health.HealthChecked;
import io.vertigo.core.analytics.health.HealthMeasure;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.VUserException;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.locale.LocaleMessageKey;
import io.vertigo.core.param.ParamValue;
import io.vertigo.datastore.filestore.model.VFile;
import io.vertigo.datastore.filestore.util.VFileUtil;
import io.vertigo.social.impl.mail.MailPlugin;
import io.vertigo.social.impl.mail.Resources;
import io.vertigo.social.mail.Mail;

/**
 * Plugin de gestion des mails, pour l'implémentation du jdk.
 *
 * @author npiedeloup
 */
public final class JavaxMailPlugin implements MailPlugin {

	/** Nom du composant. */
	public static final String HEALTH_COMPONENT_NAME = "Mailer";

	private final String charset;
	private final MailSessionConnector mailSessionConnector;
	private final boolean developmentMode;
	private final String developmentMailTo;

	/**
	 * Crée le plugin d'envoie de mail.
	 *
	 * @param fileManager Manager de gestion des fichiers
	 * @param mailSessionConnector Provider d'accès au MailSession
	 * @param developmentMode Indique s'il le mode developpement est activé (surcharge des emails destinataires)
	 * @param developmentMailTo Email destinataire forcé pour développement
	 * @param charsetOpt charset to use, default is ISO-8859-1
	 */
	@Inject
	public JavaxMailPlugin(
			final List<MailSessionConnector> mailSessionConnectors,
			@ParamValue("connectorName") final Optional<String> connectorNameOpt,
			@ParamValue("developmentMode") final boolean developmentMode,
			@ParamValue("developmentMailTo") final String developmentMailTo,
			@ParamValue("charset") final Optional<String> charsetOpt) {
		Assertion.check()
				.isNotNull(connectorNameOpt)
				.isNotNull(mailSessionConnectors)
				.isNotBlank(developmentMailTo);
		//-----
		final String connectorName = connectorNameOpt.orElse("main");
		mailSessionConnector = mailSessionConnectors.stream()
				.filter(connector -> connectorName.equals(connector.getName()))
				.findFirst().get();
		this.developmentMailTo = developmentMailTo;
		this.developmentMode = developmentMode;
		charset = charsetOpt.orElseGet(StandardCharsets.ISO_8859_1::name);
	}

	/** {@inheritDoc} */
	@Override
	public void sendMail(final Mail mail) {
		Assertion.check().isNotNull(mail);
		//-----
		final Session session = mailSessionConnector.getClient();
		try {
			final Message message = createMessage(mail, session);
			Transport.send(message);
		} catch (final MessagingException e) {
			throw createMailException(Resources.TEMPO_MAIL_SERVER_TIMEOUT, e, session.getProperty("mail.host"), session.getProperty("mail.port"));
		} catch (final UnsupportedEncodingException e) {
			throw WrappedException.wrap(e, "Probleme d'encodage lors de l'envoi du mail");
		}
	}

	private Message createMessage(final Mail mail, final Session session) throws MessagingException, UnsupportedEncodingException {
		final Message message = new MimeMessage(session);
		setFromAddress(mail.fromAddress(), message);
		if (mail.replyTo() != null) {
			setReplyToAddress(mail.replyTo(), message);
		}
		setToAddress(mail.toAddresses(), message);
		setCcAddress(mail.ccAddresses(), message);
		if (mail.subject() != null) {
			message.setSubject(MimeUtility.encodeWord(mail.subject(), charset, "Q"));
		}
		message.setHeader("X-Mailer", "Java");
		message.setSentDate(new Date());
		final List<VFile> attachments = mail.attachments();
		if (attachments.isEmpty()) {
			setBodyContent(mail.textContent(), mail.htmlContent(), message);
		} else {
			final Multipart multiPart = new MimeMultipart();
			final BodyPart bodyPart = new MimeBodyPart();
			setBodyContent(mail.textContent(), mail.htmlContent(), bodyPart);
			multiPart.addBodyPart(bodyPart);
			for (final VFile vFile : attachments) {
				final BodyPart bodyFile = createBodyFile(vFile);
				multiPart.addBodyPart(bodyFile);
			}
			message.setContent(multiPart);
		}
		return message;
	}

	private static void setFromAddress(final String from, final Message message) throws MessagingException {
		Assertion.check()
				.isNotNull(from)
				.isNotNull(message);
		//-----
		try {
			message.setFrom(createInternetAddress(from));
		} catch (final AddressException e) {
			// on catch ici, pour pouvoir indiquer l'adresse qui pose pb
			throw createMailException(Resources.TEMPO_MAIL_ADRESS_MAIL_INVALID, e, from);
		}
	}

	private static void setReplyToAddress(final String replyTo, final Message message) throws MessagingException {
		Assertion.check()
				.isNotNull(message)
				.isNotNull(replyTo);
		//-----
		try {
			final InternetAddress[] replyToArray = { createInternetAddress(replyTo) };
			message.setReplyTo(replyToArray);
		} catch (final AddressException e) {
			// on catch ici, pour pouvoir indiquer l'adresse qui pose pb
			throw createMailException(Resources.TEMPO_MAIL_ADRESS_MAIL_INVALID, e, replyTo);
		}
	}

	private static InternetAddress createInternetAddress(final String address) throws AddressException {
		final InternetAddress internetAddress = new InternetAddress(address);
		internetAddress.validate();
		return internetAddress;
	}

	private void setToAddress(final List<String> addressList, final Message message) throws MessagingException {
		setDestAddress(addressList, message, Message.RecipientType.TO);
	}

	private void setCcAddress(final List<String> addressList, final Message message) throws MessagingException {
		if (!addressList.isEmpty()) {
			setDestAddress(addressList, message, Message.RecipientType.CC);
		}
	}

	private void setDestAddress(final List<String> addressList, final Message message, final Message.RecipientType type) throws MessagingException {
		Assertion.check()
				.isNotNull(addressList)
				.isFalse(addressList.isEmpty(), "La liste des destinataires ne doit pas être vide")
				.isNotNull(message);
		//-----
		final InternetAddress[] addresses = new InternetAddress[addressList.size()];
		for (int i = 0; i < addressList.size(); i++) {
			try {
				addresses[i] = createInternetAddress(addressList.get(i));
				if (developmentMode) {
					// en mode debug on change l'adresse, mais on laisse le nom.
					addresses[i].setAddress(developmentMailTo);
				}
			} catch (final AddressException e) {
				// on catch ici, pour pouvoir indiquer l'adresse qui pose pb
				throw createMailException(Resources.TEMPO_MAIL_ADRESS_MAIL_INVALID, e, addressList.get(i));
			}
		}
		message.setRecipients(type, addresses);
	}

	private void setBodyContent(final String textContent, final String htmlContent, final Part bodyPart) throws MessagingException {
		Assertion.check()
				.isTrue(textContent != null || htmlContent != null, "Le mail n'a pas de contenu, ni en text, ni en html")
				.isNotNull(bodyPart);
		//-----
		if (textContent != null && htmlContent != null) {
			final Multipart multipart = new MimeMultipart("alternative");
			final BodyPart plainMessageBodyPart = new MimeBodyPart();
			plainMessageBodyPart.setContent(textContent, "text/plain; charset=" + charset);
			multipart.addBodyPart(plainMessageBodyPart);
			final BodyPart htmlMessageBodyPart = new MimeBodyPart();
			htmlMessageBodyPart.setContent(htmlContent, "text/html; charset=" + charset);
			multipart.addBodyPart(htmlMessageBodyPart);
			bodyPart.setContent(multipart);
		} else if (textContent != null) {
			bodyPart.setText(textContent);
		} else if (htmlContent != null) {
			bodyPart.setContent(htmlContent, "text/html; charset=" + charset);
		}
	}

	private static BodyPart createBodyFile(final VFile vFile) throws MessagingException {
		try {
			final File file = VFileUtil.obtainReadOnlyPath(vFile).toFile();
			final MimeBodyPart bodyFile = new MimeBodyPart();
			bodyFile.attachFile(file);
			bodyFile.setFileName(vFile.getFileName());
			return bodyFile;
		} catch (final IOException e) {
			throw WrappedException.wrap(e, "Can't read attached file");
		}
	}

	private static VUserException createMailException(final LocaleMessageKey messageKey, final MessagingException messagingException, final Serializable... params) {
		final VUserException mailException = new VUserException(messageKey, params);
		mailException.initCause(messagingException);
		return mailException;
	}

	/**
	 * Test de la connexion SMTP.
	 *
	 * @return HealthMeasure
	 */
	@HealthChecked(feature = HEALTH_COMPONENT_NAME, name = "connection")
	public HealthMeasure checkConnexion() {
		// -----
		try {
			final Session session = mailSessionConnector.getClient();
			try (final Transport transport = session.getTransport()) {
				// On tente la connexion
				transport.connect();
			} // La connexion s'est passée correctement. On peut la fermer
				// si on est ici, c'est que tout va bien
			return HealthMeasure.builder().withGreenStatus("Connection OK to Mail Server " + session.getProperty("mail.host")).build();
		} catch (final Exception e) {
			return HealthMeasure.builder().withRedStatus("Can't connect Mail Server").build();
		}
	}
}
