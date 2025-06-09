package com.hyd.acornai.kb;

import java.util.List;

/**
 * 知识库数据访问接口。
 * <p>
 * 该接口定义了对向量数据库中 {@link Article} 对象的基础增删改查（CRUD）操作。
 * 它作为数据访问层，不包含任何业务逻辑，特别是“策展”（Curation）相关的逻辑。
 * 实现该接口的类将负责与具体的向量数据库（如 SimpleVectorStore, Milvus, Chroma 等）进行交互。
 */
public interface AcordKnowledgeBase {

  /**
   * 向知识库中添加单个 Article。
   *
   * @param article 要添加的文章对象，其 id 字段建议由调用方预先生成并赋值。
   */
  void add(Article article);

  /**
   * 向知识库中添加多个 Article。
   *
   * @param articles 要添加的文章对象列表，其 id 字段建议由调用方预先生成并赋值。
   */
  void add(List<Article> articles);

  /**
   * 根据 ID 从知识库中删除一个 Article。
   *
   * @param articleId 要删除的文章 ID。
   */
  void delete(String articleId);

  /**
   * 根据 ID 批量从知识库中删除多个 Article。
   *
   * @param articleIds 要删除的文章 ID 列表。
   */
  void delete(List<String> articleIds);

  /**
   * 根据 ID 更新知识库中的一个 Article。
   * <p>
   * 在向量数据库中，更新操作通常是通过“先删除旧文档，再添加新文档”的方式实现的。
   *
   * @param article 包含更新后内容的文章对象，其 id 必须是已存在的文章 ID。
   */
  void update(Article article);

  /**
   * 根据给定的文本内容，在知识库中查找最相似的 Article。
   * 这是向量检索的核心功能。
   *
   * @param queryText 用于查询的文本。
   * @param topK      希望返回的最相似结果的数量。
   *
   * @return 按相似度降序排列的 Article 列表。
   */
  List<Article> findSimilar(String queryText, int topK);

  /**
   * 根据给定的文本内容，在知识库中查找相似度在指定阈值之上的 Article。
   *
   * @param queryText 用于查询的文本。
   * @param topK      希望返回的最相似结果的最大数量。
   * @param threshold 相似度阈值（通常在 0 到 1 之间），只有相似度得分高于此值的文章才会被返回。
   *
   * @return 按相似度降序排列的 Article 列表。
   */
  List<Article> findSimilar(String queryText, int topK, double threshold);

}
