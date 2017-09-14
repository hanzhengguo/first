package cn.itcast.CreateIndex;

import java.io.File;
import java.io.Reader;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class CreateIndexTest {
	/*
	 * 
	 *
	第二步：开始创建索引
		1）采集原始数据
		2）创建document对象
			根据业务需求创建Field域对象来保存原始数据中的各部分内容
			（参数1：域名、参数2：域值、参数3：是否存储）
			把上面创建好的Field对象添加进document对象中。
		3）用IndexWriter对象创建索引
			（添加过程：用IndexWriter对象添加并分析文档对象，然后创建索引，并写入索引库）
第三步：关闭IndexWriter对象（关闭中带有提交处理）
	 */
	
	/**
	 * 创建IndexWriter(创建索引准备工作)
	 * @return
	 */
	private IndexWriter createIndexWriter(String path,Analyzer analyzer) throws Exception{
		//创建Directory对象
		Directory dir = FSDirectory.open(new File(path));
		//索引库还可以存放到内存中
		//创建一个标准的分析器
		//创建IndexWriterConfig对象
		//参数1 Lucene 的版本信息 ，可以选择对应的Lucene版本也可以使用LATEST
		//参数2 分析器对象
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		//创建IndexWriter对象
		return new IndexWriter(dir, config);
		
	}
	
	/**
	 * 创建索引
	 */
	@Test
	public void CreateIndexTest() throws Exception{
		//创建IndexWriter 
		//IndexWriter indexWriter =createIndexWriter("D:\\JAVA\\项目实战2\\传智JavaEE就业262期-lucene\\资料\\jar\\lucene\\index",new StandardAnalyzer());
		IndexWriter indexWriter =createIndexWriter("D:\\JAVA\\项目实战2\\传智JavaEE就业262期-lucene\\资料\\jar\\lucene\\indexIk",new IKAnalyzer());
		//开始创建索引  
		//采集原始数据（从指定的目录下取的文件对象的集合）
		File dirSoure = new File("D:\\JAVA\\项目实战2\\传智JavaEE就业262期-lucene\\资料\\searchsource");
		//遍历文件对象列表
		for(File f : dirSoure.listFiles()){
			//文件名
			String fileName = f.getName();
			//文件内容
			String fileContent =FileUtils.readFileToString(f, "utf-8");
			//文件路径
			String filePath =f.getPath();
			//文件大小
			long fileSize =FileUtils.sizeOf(f);
			//创建文件名域：参数1：域的名称 参数2 ：域的内容 ， 参数3 是否存储
			TextField fileNameField = new TextField("filename", fileName, Store.YES);
			//创建文件内容域
			TextField fileContenField = new TextField("contet", fileContent, Store.YES);
			//创建文件路径
			TextField filePathField = new TextField("path", filePath, Store.YES);
			//创建文件大小域
			TextField fileSizeField = new TextField("size", String.valueOf(fileSize), Store.YES);
			//创建document对象
			Document document = new Document();
			document.add(fileNameField);
			document.add(fileContenField);
			document.add(filePathField);
			document.add(fileSizeField);
			//创建索引（用indexWriter对象）
			indexWriter.addDocument(document);
		}
		//第三步 关闭indexWriter 
		indexWriter.close();
	
			
		
	
	}
	
	//查看标准分析器的分词效果
	@Test
	public void testTokenStream() throws Exception {
		//创建一个标准分析器对象
		Analyzer analyzer = new StandardAnalyzer();
		//获得tokenStream对象
		//第一个参数：域名，可以随便给一个
		//第二个参数：要分析的文本内容
		TokenStream tokenStream = analyzer.tokenStream("test", "The Spring Framework provides a comprehensive programming and configuration model.");
		//添加一个引用，可以获得每个关键词
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		//添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		//将指针调整到列表的头部
		tokenStream.reset();
		//遍历关键词列表，通过incrementToken方法判断列表是否结束
		while(tokenStream.incrementToken()) {
			//关键词的起始位置
			System.out.println("start->" + offsetAttribute.startOffset());
			//取关键词
			System.out.println(charTermAttribute);
			//结束位置
			System.out.println("end->" + offsetAttribute.endOffset());
		}
		tokenStream.close();
}
	
	
}
