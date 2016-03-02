//package com.fr.main;
//
//import org.fest.swing.annotation.RunsInEDT;
//import org.fest.swing.fixture.FrameFixture;
//import org.fest.swing.fixture.JFileChooserFixture;
//import org.junit.Test;
//
//public class HTMLDocumentEditor_Test  {
//
//	
//
//
//
//	   private FrameFixture editor;
//
//
//
//	   protected void onSetUp() {
//
//	     editor = new FrameFixture(robot(), createNewEditor());
//
//	     editor.show();FestSwingJUnitTestCas
//
//	   }
//
//
//
//	   @RunsInEDT
//
//	   private static HTMLDocumentEditor createNewEditor() {
//
//	     return execute(new GuiQuery<HTMLDocumentEditor>() {
//
//	       protected HTMLDocumentEditor executeInEDT() {
//
//	         return new HTMLDocumentEditor();
//
//	       }
//
//	     });
//
//	   }
//
//
//
//	   @Test
//
//	   public void should_open_file() {
//
//	     editor.menuItemWithPath("File", "Open").click();
//
//	     JFileChooserFixture fileChooser = findFileChooser().using(robot());
//
//	     fileChooser.setCurrentDirectory(temporaryFolder())
//
//	                .selectFile(new File("helloworld.html"))
//
//	                .approve();
//
//	     assertThat(editor.textBox("document").text()).contains("Hello");
//
//	   }
//
//	}
