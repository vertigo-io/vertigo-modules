/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
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
package io.vertigo.commons.codec.crypto;

import org.junit.Assert;
import org.junit.Test;

import io.vertigo.commons.codec.AbstractCodecTest;

/**
 * Test des codesc de cryptographie.
 * @author pchretien
 */
public abstract class AbstractCryptoCodecTest extends AbstractCodecTest<byte[], byte[]> {

	/** {@inheritDoc} */
	@Override
	@Test
	public void testNull() {
		Assert.assertNull(codec.encode(null));
		Assert.assertNull(codec.decode(null));
	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testEncode() {
		for (int i = 0; i < 30000; i++) {
			Assert.assertNotNull(codec.encode(TEXT.getBytes()));
		}
	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testDecode() throws Exception {
		final byte[] encryptedValue = codec.encode(TEXT.getBytes());
		for (int i = 0; i < 30000; i++) {
			Assert.assertEquals(TEXT, new String(codec.decode(encryptedValue)));
		}
	}

	/** {@inheritDoc} */
	@Override
	@Test(expected = RuntimeException.class)
	public void testFailDecode() throws Exception {
		// object ne correspondant pas à une classe;
		final byte[] s = "qdfsdf".getBytes();
		codec.decode(s);
	}
}
