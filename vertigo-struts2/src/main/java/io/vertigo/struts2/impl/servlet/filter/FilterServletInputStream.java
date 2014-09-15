package io.vertigo.struts2.impl.servlet.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

/**
 * Input stream pour un filtre implémentant ServletInputStream à partir d'un InputStream.
 * @author Emeric Vernat
 */
class FilterServletInputStream extends ServletInputStream {
	private final InputStream in;

	/**
	 * Constructeur.
	 * @param in InputStream
	 */
	public FilterServletInputStream(final InputStream in) {
		super();
		this.in = in;
	}

	/**
	 * Constructeur.
	 * @param in ServletInputStream
	 */
	public FilterServletInputStream(final ServletInputStream in) {
		super();
		this.in = in;
	}

	/**
	 * Lit l'octet suivant dans l'input stream. L'octet est retourné en tant que type int dans l'intervalle 0 à 255,
	 * ou -1 si la fin du flux a été atteinte.
	 * @return int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public int read() throws IOException {
		return in.read();
	}

	/**
	 * Lit un certain nombre d'octets depuis l'input stream et stocke ces octets dans le tableau sp�cifi� en paramètre
	 * Le nombre d'octets lus est renvoyé en retour, ou -1 si la fin du flux a été atteinte.
	 * @param bytes bytes[]
	 * @return int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(final byte[] bytes) throws IOException {
		return in.read(bytes);
	}

	/**
	 * Lit jusqu'à <code>len</code> octets de données depuis l'input stream dans un tableau de bytes en commençant
	 * à la position <code>off</code>.
	 * Le nombre d'octets effectivement lus est renvoyé en retour, ou -1 si la fin du flux a été atteinte.
	 * @param bytes bytes[]
	 * @param off int
	 * @param len int
	 * @return int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read(final byte[] bytes, final int off, final int len) throws IOException {
		return in.read(bytes, off, len);
	}

	/**
	 * Passe et �limine <code>count</code> octets de cet input stream.
	 * Le nombre d'octets effectivement passés est renvoyé en retour.
	 * @param count int
	 * @return int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public long skip(final long count) throws IOException {
		return in.skip(count);
	}

	/**
	 * Retourne le nombre d'octets qui peuvent être lus (ou passés) depuis cet input stream sans bloquage particulier
	 * de l'appelant suivant (ce thread ou un autre) d'une méthode sur cet input stream.
	 * (L'implémentation de la class InputStream retourne toujours 0.)
	 * @return int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public int available() throws IOException {
		return in.available();
	}

	/**
	 * Ferme cet input stream.
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 */
	@Override
	public void close() throws IOException {
		in.close();
	}

	/**
	 * Marque la position courante dans cet input stream. Un appel à la méthode reset repositionnera ce flux
	 * à la position marqu�e.
	 * L'agument readLimit indique à cet inputStream d'accepter autant d'octets à lire avant que la position
	 * marqu�e ne devienne invalide.
	 * (Cette méthode devrait être synchronized mais le synchronized du InputStream suffit).
	 * @param readlimit int
	 * @see java.io.InputStream#reset()
	 */
	@Override
	synchronized public void mark(final int readlimit) {
		in.mark(readlimit);
	}

	/**
	 * Repositionne le flux à la position pr�c�demment marqu�e.
	 * (Cette méthode devrait être synchronized mais celui du InputStream suffit).
	 * @throws  IOException  Si le flux n'a pas été marqué ou si la marque est devenue invalide
	 * @see java.io.InputStream#mark(int)
	 * @see java.io.IOException
	 */
	@Override
	synchronized public void reset() throws IOException {
		in.reset();
	}

	/**
	 * Teste si cet input stream supporte les m�thodes <code>mark</code> et <code>reset</code>.
	 * @return boolean
	 * @see java.io.InputStream#mark(int)
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public boolean markSupported() {
		return in.markSupported();
	}

	/**
	 * Lit l'input stream, une ligne à la fois.
	 * Le nombre d'octets lus est renvoyé en retour, ou -1 si la fin du flux a été atteinte.
	 * @param buf byte[]
	 * @param off int
	 * @param len int
	 * @return int
	 * @throws java.io.IOException   Erreur d'entrée/sortie
	 * @see javax.servlet.ServletInputStream#readLine(byte[], int, int)
	 */
	@Override
	public int readLine(final byte[] buf, final int off, final int len) throws IOException {
		if (in instanceof ServletInputStream) {
			final ServletInputStream sIn = (ServletInputStream) in;
			return sIn.readLine(buf, off, len);
		}
		return super.readLine(buf, off, len);
	}
}