package shoppingmall.userservice.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTelNo is a Querydsl query type for TelNo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTelNo extends BeanPath<TelNo> {

    private static final long serialVersionUID = 1283349343L;

    public static final QTelNo telNo1 = new QTelNo("telNo1");

    public final StringPath telNo = createString("telNo");

    public QTelNo(String variable) {
        super(TelNo.class, forVariable(variable));
    }

    public QTelNo(Path<? extends TelNo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTelNo(PathMetadata metadata) {
        super(TelNo.class, metadata);
    }

}

