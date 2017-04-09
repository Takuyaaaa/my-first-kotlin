package com.github.asufana.domain.model.comment

import com.github.asufana.domain.base.entity.AbstractEntity
import com.github.asufana.domain.base.util.resolve
import com.github.asufana.domain.model.comment.repo.CommentRepo
import com.github.asufana.domain.model.comment.vo.CommentId
import com.github.asufana.domain.model.comment.vo.CommentName
import org.hibernate.internal.util.StringHelper.*
import javax.persistence.Entity
import javax.persistence.Table

/** 投稿コメント */
@Entity
@Table(name="posts")
class Comment private constructor() : AbstractEntity() {

    lateinit var name: CommentName

    constructor(name: CommentName): this() {
        this.name = name

        isSatisfied()
    }

    override fun isSatisfied() {
        assert(this.name != null && isNotEmpty(name.value))
    }

    fun id(): CommentId {
        return CommentId(this.id)
    }

    fun save(): Comment {
        isSatisfied()
        return repo().saveAndFlush(this)
    }

    private fun repo(): CommentRepo {
        return resolve(CommentRepo::class.java)
    }
}