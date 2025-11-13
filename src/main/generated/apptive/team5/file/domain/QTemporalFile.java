package apptive.team5.file.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTemporalFile is a Querydsl query type for TemporalFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemporalFile extends EntityPathBase<TemporalFile> {

    private static final long serialVersionUID = -1549830507L;

    public static final QTemporalFile temporalFile = new QTemporalFile("temporalFile");

    public final apptive.team5.global.entity.QBaseTimeEntity _super = new apptive.team5.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDateTime = _super.updateDateTime;

    public QTemporalFile(String variable) {
        super(TemporalFile.class, forVariable(variable));
    }

    public QTemporalFile(Path<? extends TemporalFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTemporalFile(PathMetadata metadata) {
        super(TemporalFile.class, metadata);
    }

}

