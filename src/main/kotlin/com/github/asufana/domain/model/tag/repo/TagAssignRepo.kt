package com.github.asufana.domain.model.tag.repo

import com.github.asufana.domain.base.util.resolve
import com.github.asufana.domain.model.post.Post
import com.github.asufana.domain.model.post.collection.PostCollection
import com.github.asufana.domain.model.tag.Tag
import com.github.asufana.domain.model.tag.TagAssign
import com.github.asufana.domain.model.tag.collection.TagCollection
import com.github.asufana.domain.model.tag.collection.toCollection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
private interface TagAssignRepoBase : JpaRepository<TagAssign, Long>

class TagAssignRepo {

    companion object {
        private fun repo(): TagAssignRepoBase = resolve(TagAssignRepoBase::class.java)
        private fun em(): EntityManager = resolve(EntityManager::class.java)

        fun findBy(post: Post, tag: Tag): TagAssign? {
            val list = em()
                    .createQuery("from TagAssign where " +
                            "post = :post and tag = :tag", TagAssign::class.java)
                    .setParameter("post", post)
                    .setParameter("tag", tag)
                    .resultList
            return if (list.isEmpty()) null else list[0]
        }

        /** タグ付け */
        fun assign(post: Post, tag: Tag): TagAssign {
            val tagAssign = findBy(post, tag)
            if (tagAssign != null) {
                return tagAssign
            }
            return TagAssign(post, tag).save()
        }

        /** タグ付け解除 */
        fun unAssign(post: Post, tag: Tag) {
            val tagAssign = findBy(post, tag)
            tagAssign?.delete()
        }

        /** 関連付けられたタグ一覧の取得 */
        internal fun findBy(post: Post): TagCollection {
            val tagAssignList = em()
                    .createQuery("from TagAssign where post = :post", TagAssign::class.java)
                    .setParameter("post", post)
                    .resultList
                    .toCollection()
            return tagAssignList.toTagCollection()
        }

        /** 関連付けられた投稿一覧の取得 */
        internal fun findBy(tag: Tag): PostCollection {
            val tagAssignList = em()
                    .createQuery("from TagAssign where tag = :tag", TagAssign::class.java)
                    .setParameter("tag", tag)
                    .resultList
                    .toCollection()
            return tagAssignList.toPostCollection()
        }

        fun save(tagAssign: TagAssign): TagAssign {
            return repo().saveAndFlush(tagAssign)
        }

        fun delete(tagAssign: TagAssign) {
            return repo().delete(tagAssign)
        }
    }

}
