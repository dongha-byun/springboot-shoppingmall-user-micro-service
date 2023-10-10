package shoppingmall.userservice.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1842556744L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final shoppingmall.userservice.QBaseEntity _super = new shoppingmall.userservice.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isLock = createBoolean("isLock");

    public final QLoginInfo loginInfo;

    public final DateTimePath<java.time.LocalDateTime> signUpDate = createDateTime("signUpDate", java.time.LocalDateTime.class);

    public final QTelNo telNo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public final QUserGradeInfo userGradeInfo;

    public final StringPath userName = createString("userName");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.loginInfo = inits.isInitialized("loginInfo") ? new QLoginInfo(forProperty("loginInfo")) : null;
        this.telNo = inits.isInitialized("telNo") ? new QTelNo(forProperty("telNo")) : null;
        this.userGradeInfo = inits.isInitialized("userGradeInfo") ? new QUserGradeInfo(forProperty("userGradeInfo")) : null;
    }

}

