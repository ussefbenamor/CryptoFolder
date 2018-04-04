package com.nafaaazaiez.cryptofolder.db;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by Ussef on 28/01/2018.
 */
@Entity(
    // Flag to make an entity "active": Active entities have update,
    // delete, and refresh methods.
    active = true,

    // Specifies the name of the table in the database.
    // By default, the name is based on the entities class name.
    nameInDb = "CRYPTO_ALERT",

    // Whether an all properties constructor should be generated.
    // A no-args constructor is always required.
    generateConstructors = true,

    // Whether getters and setters for properties should be generated if missing.
    generateGettersSetters = true
)
public class Alert {

    @Id(autoincrement = true)
    private Long id;
    private double above;
    private double below;
    private boolean enabled;
    private String currency;
    private Long coinId;
    @ToOne(joinProperty = "coinId")
    private Coin coin;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1062490079)
    private transient AlertDao myDao;
    @Generated(hash = 1742069775)
    private transient Long coin__resolvedKey;

    @Generated(hash = 772599468)
    public Alert(Long id, double above, double below, boolean enabled,
        String currency, Long coinId) {
        this.id = id;
        this.above = above;
        this.below = below;
        this.enabled = enabled;
        this.currency = currency;
        this.coinId = coinId;
    }

    @Generated(hash = 1500134595)
    public Alert() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAbove() {
        return this.above;
    }

    public void setAbove(double above) {
        this.above = above;
    }

    public double getBelow() {
        return this.below;
    }

    public void setBelow(double below) {
        this.below = below;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getCoinId() {
        return this.coinId;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1185724968)
    public Coin getCoin() {
        Long __key = this.coinId;
        if (coin__resolvedKey == null || !coin__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CoinDao targetDao = daoSession.getCoinDao();
            Coin coinNew = targetDao.load(__key);
            synchronized (this) {
                coin = coinNew;
                coin__resolvedKey = __key;
            }
        }
        return coin;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 312223032)
    public void setCoin(Coin coin) {
        synchronized (this) {
            this.coin = coin;
            coinId = coin == null ? null : coin.getId();
            coin__resolvedKey = coinId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2046146063)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAlertDao() : null;
    }

}
