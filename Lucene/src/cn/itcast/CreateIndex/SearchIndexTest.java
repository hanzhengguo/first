package cn.itcast.CreateIndex;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class SearchIndexTest {
	/*
	 * 
	 * 查询索引
	 */
	@Test
	public void testSearchIndex() throws Exception {
		//第一步： 查询准备工作 
		//创建Directory对象
		Directory dir = FSDirectory.open( new File("D:\\JAVA\\项目实战2\\传智JavaEE就业262期-lucene\\资料\\jar\\lucene\\index"));
		//创建IndexReder对象
		IndexReader reader = DirectoryReader.open(dir);
		//创建IndexSercher对象
		IndexSearcher searcher = new IndexSearcher(reader);
		//创建查询条件对象
		TermQuery query = new TermQuery(new Term("filename", "apache"));
		//第三步 执行查询（参数1  查询条件对象， 参数2 查询结果返回的最大值）
		TopDocs topDocs = searcher.search(query, 20);
		//第四步 处理查询结果
		//输出结果数量
		System.out.println("查询的结果数量："+topDocs.totalHits);
		//取得结果集
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//遍历结果集
		for (ScoreDoc scoreDoc : scoreDocs) {
			//根据文档对象id取得文档对象
			Document doc = searcher.doc(scoreDoc.doc);
			//打印搜索结果内容
			//文件名称
			System.out.println("文件名称"+doc.get("filename"));
			//文件路径
			System.out.println("文件路径"+doc.get("path"));
			//文件大小
			System.out.println("文件大小"+doc.get("size"));
		}
		//关闭IndexReader对象
		reader.close();
	}

}
