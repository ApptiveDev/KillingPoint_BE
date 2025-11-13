package apptive.team5.diary.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiaryLikeEntity is a Querydsl query type for DiaryLikeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDiaryLikeEntity extends EntityPathBase<DiaryLikeEntity> {

    private static final long serialVersionUID = 58636669L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDiaryLikeEntity diaryLikeEntity = new QDiaryLikeEntity("diaryLikeEntity");

    public final QDiaryEntity diary;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final apptive.team5.user.domain.QUserEntity user;

    public QDiaryLikeEntity(String variable) {
        this(DiaryLikeEntity.class, forVariable(variable), INITS);
    }

    public QDiaryLikeEntity(Path<? extends DiaryLikeEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDiaryLikeEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDiaryLikeEntity(PathMetadata metadata, PathInits inits) {
        this(DiaryLikeEntity.class, metadata, inits);
    }

    public QDiaryLikeEntity(Class<? extends DiaryLikeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.diary = inits.isInitialized("diary") ? new QDiaryEntity(forProperty("diary"), inits.get("diary")) : null;
        this.user = inits.isInitialized("user") ? new apptive.team5.user.domain.QUserEntity(forProperty("user"), inits.get("user")) : null;
    }

}

