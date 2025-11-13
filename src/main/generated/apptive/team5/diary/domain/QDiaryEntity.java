package apptive.team5.diary.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiaryEntity is a Querydsl query type for DiaryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDiaryEntity extends EntityPathBase<DiaryEntity> {

    private static final long serialVersionUID = -806849402L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDiaryEntity diaryEntity = new QDiaryEntity("diaryEntity");

    public final apptive.team5.global.entity.QBaseTimeEntity _super = new apptive.team5.global.entity.QBaseTimeEntity(this);

    public final StringPath albumImageUrl = createString("albumImageUrl");

    public final StringPath artist = createString("artist");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final StringPath duration = createString("duration");

    public final StringPath end = createString("end");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath musicTitle = createString("musicTitle");

    public final EnumPath<DiaryScope> scope = createEnum("scope", DiaryScope.class);

    public final StringPath start = createString("start");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDateTime = _super.updateDateTime;

    public final apptive.team5.user.domain.QUserEntity user;

    public final StringPath videoUrl = createString("videoUrl");

    public QDiaryEntity(String variable) {
        this(DiaryEntity.class, forVariable(variable), INITS);
    }

    public QDiaryEntity(Path<? extends DiaryEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDiaryEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDiaryEntity(PathMetadata metadata, PathInits inits) {
        this(DiaryEntity.class, metadata, inits);
    }

    public QDiaryEntity(Class<? extends DiaryEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new apptive.team5.user.domain.QUserEntity(forProperty("user"), inits.get("user")) : null;
    }

}

