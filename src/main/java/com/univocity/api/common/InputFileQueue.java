/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.api.common;


import com.univocity.api.*;

import java.io.*;
import java.nio.charset.*;

/**
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public class InputFileQueue extends InputQueue<FileProvider> {

	public void addFile(File file) {
		this.addFile(file, (Charset) null);
	}

	public void addFile(File file, String encoding) {
		addFile(new FileProvider(file, encoding));
	}

	public void addFile(File file, Charset encoding) {
		this.addFile(new FileProvider(file, encoding));
	}

	public void addFile(String filePath) {
		this.addFile(filePath, (Charset) null);
	}

	public void addFile(String filePath, String encoding) {
		addFile(new File(filePath, encoding));
	}

	public void addFile(String filePath, Charset encoding) {
		addFile(new FileProvider(filePath, encoding));
	}

	public void addFile(FileProvider fileProvider) {
		offer(fileProvider);
	}

	@Override
	protected Reader open(FileProvider input) {
		return Builder.build(Reader.class, input);
	}
}
