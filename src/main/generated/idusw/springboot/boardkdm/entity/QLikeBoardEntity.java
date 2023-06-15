package idusw.springboot.boardkdm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikeBoardEntity is a Querydsl query type for LikeBoardEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeBoardEntity extends EntityPathBase<LikeBoardEntity> {

    private static final long serialVersionUID = -1686168472L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikeBoardEntity likeBoardEntity = new QLikeBoardEntity("likeBoardEntity");

    public final QBoardEntity board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMemberEntity member;

    public QLikeBoardEntity(String variable) {
        this(LikeBoardEntity.class, forVariable(variable), INITS);
    }

    public QLikeBoardEntity(Path<? extends LikeBoardEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikeBoardEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikeBoardEntity(PathMetadata metadata, PathInits inits) {
        this(LikeBoardEntity.class, metadata, inits);
    }

    public QLikeBoardEntity(Class<? extends LikeBoardEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoardEntity(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new QMemberEntity(forProperty("member")) : null;
    }

}

