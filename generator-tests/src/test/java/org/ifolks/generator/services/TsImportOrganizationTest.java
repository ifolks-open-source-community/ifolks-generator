package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ifolks.generator.skeletons.commands.impl.typed.TsFileWriteCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TsImportOrganizationTest {

	private static class DummyTsCommand extends TsFileWriteCommand {
		public DummyTsCommand(String folderName, String fileName) {
			super(folderName, fileName);
		}

		@Override
		protected void fetchSpecificImports() {
			// Subclass hook
		}

		@Override
		protected void writeContent() throws IOException {
			writeImports();
			writeLine("export class Dummy {}");
		}
	}

	@Test
	public void testSymbolDeduplicationAndMerging() {
		DummyTsCommand cmd = new DummyTsCommand("test", "test");
		Set<String> rawImports = new HashSet<>(Arrays.asList(
				"import { Component, OnInit, ViewChild } from '@angular/core';",
				"import { ChangeDetectorRef, OnInit } from '@angular/core';"
		));

		List<String> organized = cmd.organizeImports(rawImports);

		assertEquals(1, organized.size(), "Should merge multiple imports from the same module into one line");
		assertEquals("import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';", organized.get(0));
	}

	@Test
	public void testQuoteNormalization() {
		DummyTsCommand cmd = new DummyTsCommand("test", "test");
		Set<String> rawImports = new HashSet<>(Arrays.asList(
				"import { ScrollForm } from \"src/app/core/models/ScrollForm\";",
				"import { ScrollView } from 'src/app/core/models/ScrollView';"
		));

		List<String> organized = cmd.organizeImports(rawImports);

		assertEquals(2, organized.size());
		for (String imp : organized) {
			assertFalse(imp.contains("\""), "Double quotes must be normalized to single quotes: " + imp);
			assertTrue(imp.contains("'"), "Single quotes must be used: " + imp);
		}
	}

	@Test
	public void testDeterministicModuleSorting() {
		DummyTsCommand cmd = new DummyTsCommand("test", "test");
		Set<String> rawImports = new HashSet<>(Arrays.asList(
				"import { FormBuilder } from '@angular/forms';",
				"import { Component } from '@angular/core';",
				"import { Subscription } from 'rxjs';",
				"import { CommonModule } from '@angular/common';"
		));

		List<String> organized = cmd.organizeImports(rawImports);

		assertEquals(4, organized.size());
		assertTrue(organized.get(0).contains("@angular/common"));
		assertTrue(organized.get(1).contains("@angular/core"));
		assertTrue(organized.get(2).contains("@angular/forms"));
		assertTrue(organized.get(3).contains("rxjs"));
	}

	@Test
	public void testOtherImportTypesPreserved() {
		DummyTsCommand cmd = new DummyTsCommand("test", "test");
		Set<String> rawImports = new HashSet<>(Arrays.asList(
				"import 'zone.js';",
				"import * as moment from \"moment\";",
				"import { Component } from '@angular/core';"
		));

		List<String> organized = cmd.organizeImports(rawImports);

		assertEquals(3, organized.size());
		assertTrue(organized.stream().anyMatch(i -> i.equals("import { Component } from '@angular/core';")));
		assertTrue(organized.stream().anyMatch(i -> i.equals("import * as moment from 'moment';")));
		assertTrue(organized.stream().anyMatch(i -> i.equals("import 'zone.js';")));
	}

	@Test
	public void testOtherImportsDeduplicatedAfterQuoteNormalization() {
		DummyTsCommand cmd = new DummyTsCommand("test", "test");
		Set<String> rawImports = new HashSet<>(Arrays.asList(
				"import 'zone.js';",
				"import \"zone.js\"",
				"import * as moment from \"moment\";",
				"import * as moment from 'moment';"
		));

		List<String> organized = cmd.organizeImports(rawImports);

		assertEquals(2, organized.size(), "Side-effect and namespace imports must be deduplicated after quote normalization");
		assertTrue(organized.contains("import * as moment from 'moment';"));
		assertTrue(organized.contains("import 'zone.js';"));
	}

	@Test
	public void testMixedDefaultAndTypeImportsDocumentedBehavior() {
		DummyTsCommand cmd = new DummyTsCommand("test", "test");
		Set<String> rawImports = new HashSet<>(Arrays.asList(
				"import Foo, { Bar } from \"./foo\";",
				"import { Baz } from './foo';",
				"import type { MyType } from './types';",
				"import { MyVal } from './types';"
		));

		List<String> organized = cmd.organizeImports(rawImports);

		// Mixed default + named import falls into otherImports (normalized but kept separate from pure named import)
		assertTrue(organized.contains("import Foo, { Bar } from './foo';"));
		assertTrue(organized.contains("import { Baz } from './foo';"));

		// 'import type { A }' is merged into standard named imports for the same module
		assertTrue(organized.contains("import { MyType, MyVal } from './types';"));
	}

	@Test
	public void testMultilineImportFileReading(@TempDir Path tempDir) throws IOException {
		String initialContent = String.join("\n",
				"/**",
				" * Header comment",
				" */",
				"import {",
				"  ChangeDetectorRef,",
				"  Component",
				"} from '@angular/core';",
				"import { Routes } from '@angular/router';",
				"",
				"@Component({",
				"  selector: 'app-dummy'",
				"})",
				"export class DummyComponent {}"
		);

		Path testFile = tempDir.resolve("dummy.component.ts");
		Files.writeString(testFile, initialContent);

		DummyTsCommand cmd = new DummyTsCommand(tempDir.toString(), "dummy.component") {
			@Override
			protected void fetchSpecificImports() {
				// Writer adds OnInit to @angular/core
				addImport("@angular/core", "OnInit");
			}
		};

		String generated = cmd.generateContent();

		// Check that multiline import was properly reconstructed and merged with OnInit
		assertTrue(generated.contains("import { ChangeDetectorRef, Component, OnInit } from '@angular/core';"),
				"Generated content should contain merged, single-line @angular/core import");
		assertTrue(generated.contains("import { Routes } from '@angular/router';"),
				"Generated content should preserve Routes import");
		assertFalse(generated.lines().anyMatch(l -> l.trim().equals("import {")),
				"Generated content must not contain isolated 'import {' lines");
	}
}
