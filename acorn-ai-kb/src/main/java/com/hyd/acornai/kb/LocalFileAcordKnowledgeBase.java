package com.hyd.acornai.kb;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * 基于本地文件实现的 AcordKnowledgeBase
 */
@Slf4j
public class LocalFileAcordKnowledgeBase implements AcordKnowledgeBase {

  private static final Function<Document, Article> DOCUMENT_CONVERTER = doc -> new Article(doc.getId(), doc.getText());

  /**
   * 存储本地数据文件的路径。
   */
  private final Path dataFile;

  /**
   * 用于存储和检索向量数据的 SimpleVectorStore 实例。
   */
  private final SimpleVectorStore vectorStore;

  @Getter
  private boolean empty = true;

  /**
   * 构造函数
   */
  public LocalFileAcordKnowledgeBase(SimpleVectorStore vectorStore, Path dataFile) {
    this.vectorStore = vectorStore;
    this.dataFile = dataFile;
    load();
  }

  public void load() {
    if (Files.exists(this.dataFile)) {
      this.vectorStore.load(this.dataFile.toFile());
      this.empty = false;
    }
  }

  private void addArticles(List<Article> articles) {
    if (articles == null || articles.isEmpty()) {
      return;
    }

    Function<Article, Document> docBuilder =
      article -> Document.builder().id(UUID.randomUUID().toString()).text(article.getContent()).build();

    vectorStore.add(articles.stream().map(docBuilder).toList());
    empty = false;
  }

  public void save() {
    if (this.dataFile != null) {
      this.vectorStore.save(this.dataFile.toFile());
    }
  }

  @Override
  public void add(Article article) {
    addArticles(List.of(article));
    save();
  }

  @Override
  public void add(List<Article> articles) {
    addArticles(articles);
    save();
  }

  /**
   * 从知识库中删除指定 ID 的 Article 对象。
   * 同时删除向量数据库中的记录和对应的本地文件。
   *
   * @param articleId 要删除的 Article 对象的 ID。
   */
  @Override
  public void delete(String articleId) {
    vectorStore.delete(articleId);
    save();
  }

  @Override
  public void delete(List<String> articleIds) {
    vectorStore.delete(articleIds);
    save();
  }

  /**
   * 更新知识库中指定 Article 对象的信息。
   * 先删除旧的记录，再添加新的记录。
   *
   * @param article 要更新的 Article 对象。
   */
  @Override
  public void update(Article article) {
    delete(article.getId());
    add(article);
    save();
  }

  /**
   * 根据查询文本查找最相似的 Article 对象。
   *
   * @param queryText 查询文本。
   * @param topK      返回的最相似的 Article 对象的数量。
   *
   * @return 最相似的 Article 对象列表。
   */
  @Override
  public List<Article> findSimilar(String queryText, int topK) {
    // 在向量数据库中执行相似度搜索
    var documents = vectorStore.doSimilaritySearch(
      SearchRequest.builder().query(queryText).topK(topK).build()
    );
    // 将搜索结果转换为 Article 对象列表并返回
    return documents.stream().map(DOCUMENT_CONVERTER).toList();
  }

  /**
   * 根据查询文本和相似度阈值查找最相似的 Article 对象。
   *
   * @param queryText 查询文本。
   * @param topK      返回的最相似的 Article 对象的数量。
   * @param threshold 相似度阈值，只有相似度大于该阈值的结果才会返回。
   *
   * @return 最相似的 Article 对象列表。
   */
  @Override
  public List<Article> findSimilar(String queryText, int topK, double threshold) {
    // 在向量数据库中执行相似度搜索，并设置相似度阈值
    var documents = vectorStore.doSimilaritySearch(
      SearchRequest.builder().query(queryText).topK(topK).similarityThreshold(threshold).build()
    );
    // 将搜索结果转换为 Article 对象列表并返回
    return documents.stream().map(DOCUMENT_CONVERTER).toList();
  }
}
