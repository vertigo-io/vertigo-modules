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
package io.vertigo.commons.codec.compressedSerialization;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;

import io.vertigo.commons.codec.AbstractCodecTest;
import io.vertigo.commons.codec.Codec;
import io.vertigo.commons.codec.CodecManager;

/**
 * Test du codec de compresion.
 * 
 * @author pchretien
 */
public final class CompressedSerializationCodecTest extends AbstractCodecTest<Serializable, byte[]> {

	/** {@inheritDoc} */
	@Override
	public Codec<Serializable, byte[]> obtainCodec(final CodecManager codecManager) {
		return codecManager.getCompressedSerializationCodec();
	}

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
		Assert.assertNotNull(codec.encode(TEXT.getBytes()));

	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testDecode() throws Exception {
		final byte[] encodedValue = codec.encode(TEXT.getBytes());
		Assert.assertEquals(TEXT, new String((byte[]) codec.decode(encodedValue)));

	}

	/** {@inheritDoc} */
	@Override
	@Test
	public void testFailDecode() throws Exception {
		//
	}

}
