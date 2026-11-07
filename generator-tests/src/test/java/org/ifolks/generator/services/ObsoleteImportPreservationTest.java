package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;
import org.ifolks.generator.skeletons.commands.impl.typed.TsFileWriteCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ObsoleteImportPreservationTest {

	private static class DummyBaseJavaCommand extends JavaFileWriteCommand {
		public DummyBaseJavaCommand(String folderName, String fileName) {
			super(folderName, fileName);
		}

		@Override
		public boolean supportsSpecificCode() {
			return false;
		}

		@Override
		protected void fetchSpecificImports() {
			javaImports.add("import com.example.active.CurrentEntity;");
		}

		@Override
		protected void writeContent() throws IOException {
			writeLine("package com.example;");
			skipLine();
			writeImports();
			skipLine();
			writeLine("public class DummyBaseService {}");
		}
	}

	private static class DummyCustomJavaCommand extends JavaFileWriteCommand {
		public DummyCustomJavaCommand(String folderName, String fileName) {
			super(folderName, fileName);
		}

		@Override
		public boolean supportsSpecificCode() {
			return true;
		}

		@Override
		protected void fetchSpecificImports() {
			javaImports.add("import com.example.active.CurrentEntity;");
		}

		@Override
		protected void writeContent() throws IOException {
			writeLine("package com.example;");
			skipLine();
			writeImports();
			skipLine();
			writeLine("public class DummyService {");
			writeNotOverridableContent();
			writeLine("}");
		}
	}

	private static class DummyBaseTsCommand extends TsFileWriteCommand {
		public DummyBaseTsCommand(String folderName, String fileName) {
			super(folderName, fileName);
		}

		@Override
		public boolean supportsSpecificCode() {
			return false;
		}

		@Override
		protected void fetchSpecificImports() {
			addImport("./active/active-route", "ActiveRoute");
		}

		@Override
		protected void writeContent() throws IOException {
			writeImports();
			skipLine();
			writeLine("export const routes = [];");
		}
	}

	private static class DummyCustomTsCommand extends TsFileWriteCommand {
		public DummyCustomTsCommand(String folderName, String fileName) {
			super(folderName, fileName);
		}

		@Override
		public boolean supportsSpecificCode() {
			return true;
		}

		@Override
		protected void fetchSpecificImports() {
			addImport("@angular/core", "Component");
		}

		@Override
		protected void writeContent() throws IOException {
			writeImports();
			skipLine();
			writeLine("export class DummyComponent {");
			writeNotOverridableContent();
			writeLine("}");
		}
	}

	@Test
	public void testBaseJavaClassDiscardsObsoleteImports(@TempDir Path tempDir) throws IOException {
		Path existingFile = tempDir.resolve("DummyBaseService.java");
		String existingContent = String.join("\n",
				"package com.example;",
				"",
				"import com.example.obsolete.DeletedEntity;",
				"import com.example.obsolete.OldRepository;",
				"",
				"public class DummyBaseService {}"
		);
		Files.writeString(existingFile, existingContent);

		DummyBaseJavaCommand cmd = new DummyBaseJavaCommand(tempDir.toString(), "DummyBaseService");
		String generated = cmd.generateContent();

		assertTrue(generated.contains("import com.example.active.CurrentEntity;"),
				"Generated base class should contain active import");
		assertFalse(generated.contains("DeletedEntity"),
				"Base class must NOT retain obsolete import for DeletedEntity from previous generation");
		assertFalse(generated.contains("OldRepository"),
				"Base class must NOT retain obsolete import for OldRepository from previous generation");
	}

	@Test
	public void testCustomizableJavaClassPreservesImports(@TempDir Path tempDir) throws IOException {
		Path existingFile = tempDir.resolve("DummyService.java");
		String existingContent = String.join("\n",
				"package com.example;",
				"",
				"import com.example.custom.DeveloperCustomClass;",
				"import com.example.active.CurrentEntity;",
				"",
				"public class DummyService {",
				"/* Specific Code Start */",
				"/* Specific Code End */",
				"}"
		);
		Files.writeString(existingFile, existingContent);

		DummyCustomJavaCommand cmd = new DummyCustomJavaCommand(tempDir.toString(), "DummyService");
		String generated = cmd.generateContent();

		assertTrue(generated.contains("import com.example.custom.DeveloperCustomClass;"),
				"Customizable class should preserve user-written custom developer import");
		assertTrue(generated.contains("import com.example.active.CurrentEntity;"),
				"Customizable class should contain active generator import");
	}

	@Test
	public void testBaseTsClassDiscardsObsoleteImports(@TempDir Path tempDir) throws IOException {
		Path existingFile = tempDir.resolve("dummy.routes.ts");
		String existingContent = String.join("\n",
				"import { OldDeletedRoute } from './old/old-route';",
				"",
				"export const routes = [];"
		);
		Files.writeString(existingFile, existingContent);

		DummyBaseTsCommand cmd = new DummyBaseTsCommand(tempDir.toString(), "dummy.routes");
		String generated = cmd.generateContent();

		assertTrue(generated.contains("import { ActiveRoute } from './active/active-route';"),
				"Base TS class/route should contain active import");
		assertFalse(generated.contains("OldDeletedRoute"),
				"Base TS class/route must NOT retain obsolete import from previous generation");
	}

	@Test
	public void testCustomizableTsClassPreservesAndOrganizesImports(@TempDir Path tempDir) throws IOException {
		Path existingFile = tempDir.resolve("dummy.component.ts");
		String existingContent = String.join("\n",
				"import { DeveloperHelper } from './helpers';",
				"",
				"export class DummyComponent {",
				"/* Specific Code Start */",
				"/* Specific Code End */",
				"}"
		);
		Files.writeString(existingFile, existingContent);

		DummyCustomTsCommand cmd = new DummyCustomTsCommand(tempDir.toString(), "dummy.component");
		String generated = cmd.generateContent();

		assertTrue(generated.contains("import { DeveloperHelper } from './helpers';"),
				"Customizable TS class should preserve user-written import");
		assertTrue(generated.contains("import { Component } from '@angular/core';"),
				"Customizable TS class should contain active import");
	}
}
