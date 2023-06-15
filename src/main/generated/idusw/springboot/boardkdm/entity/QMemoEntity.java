package idusw.springboot.boardkdm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemoEntity is a Querydsl query type for MemoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemoEntity extends EntityPathBase<MemoEntity> {

    private static final long serialVersionUID = 993402343L;

    public static final QMemoEntity memoEntity = new QMemoEntity("memoEntity");

    public final StringPath memoText = createString("memoText");

    public final NumberPath<Long> mno = createNumber("mno", Long.class);

    public QMemoEntity(String variable) {
        super(MemoEntity.class, forVariable(variable));
    }

    public QMemoEntity(Path<? extends MemoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemoEntity(PathMetadata metadata) {
        super(MemoEntity.class, metadata);
    }

}

