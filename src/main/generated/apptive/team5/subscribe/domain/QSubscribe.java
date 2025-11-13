package apptive.team5.subscribe.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubscribe is a Querydsl query type for Subscribe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscribe extends EntityPathBase<Subscribe> {

    private static final long serialVersionUID = -422360541L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubscribe subscribe = new QSubscribe("subscribe");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final apptive.team5.user.domain.QUserEntity subscribedTo;

    public final apptive.team5.user.domain.QUserEntity subscriber;

    public QSubscribe(String variable) {
        this(Subscribe.class, forVariable(variable), INITS);
    }

    public QSubscribe(Path<? extends Subscribe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubscribe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubscribe(PathMetadata metadata, PathInits inits) {
        this(Subscribe.class, metadata, inits);
    }

    public QSubscribe(Class<? extends Subscribe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.subscribedTo = inits.isInitialized("subscribedTo") ? new apptive.team5.user.domain.QUserEntity(forProperty("subscribedTo"), inits.get("subscribedTo")) : null;
        this.subscriber = inits.isInitialized("subscriber") ? new apptive.team5.user.domain.QUserEntity(forProperty("subscriber"), inits.get("subscriber")) : null;
    }

}

