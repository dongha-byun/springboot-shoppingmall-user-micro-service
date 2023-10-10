package shoppingmall.userservice.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserGradeInfo is a Querydsl query type for UserGradeInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserGradeInfo extends BeanPath<UserGradeInfo> {

    private static final long serialVersionUID = -1127385411L;

    public static final QUserGradeInfo userGradeInfo = new QUserGradeInfo("userGradeInfo");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final EnumPath<UserGrade> grade = createEnum("grade", UserGrade.class);

    public final NumberPath<Integer> orderCount = createNumber("orderCount", Integer.class);

    public QUserGradeInfo(String variable) {
        super(UserGradeInfo.class, forVariable(variable));
    }

    public QUserGradeInfo(Path<? extends UserGradeInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserGradeInfo(PathMetadata metadata) {
        super(UserGradeInfo.class, metadata);
    }

}

