package com.fr.main;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *     FEST-Swing是一个用于Swing GUI应用程序功能测试的java开源类库。支持模拟用户交互（鼠标操作和键盘输入）。
 *     支持JDK中的所有Swing组件。提供简洁、强大的API来创建和维护GUI功能测试。支持在HTML测试报告中嵌入GUI测试失败的截屏。
 *     能够与JUnit或TestNG一起使用。
 * @author longgangbai
 *
 */
public class CustomFrameTest { 

  private FrameFixture frame; 
  /**
   * 创建主窗体对象
   */
  @Before 
  public void setUp() { 
    frame = new FrameFixture(new HTMLDocumentEditor()); 
    frame.show(); 
  } 

  /**
   * 在@After方法中对其进行清理：
   */
  @After 
  public void tearDown() { 
    frame.cleanUp(); 
  } 

  @Test 
  public void testCopyTextToLabel() { 
    frame.textBox("input").enterText("Hello World!"); 
    frame.button("copy").click(); 
    frame.label("show").requireText("Hello World!"); 
  } 
} 