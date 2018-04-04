package com.nafaaazaiez.cryptofolder.db;


import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

/**
 * Created by Ussef on 20/01/2018.
 */
@Entity(
    // Flag to make an entity "active": Active entities have update,
    // delete, and refresh methods.
    active = true,

    // Specifies the name of the table in the database.
    // By default, the name is based on the entities class name.
    nameInDb = "CRYPTO_COIN",

    // Whether an all properties constructor should be generated.
    // A no-args constructor is always required.
    generateConstructors = true,

    // Whether getters and setters for properties should be generated if missing.
    generateGettersSetters = true
)
public class Coin {

    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String name;
    private String coinName;
    private String logoURL;
    private double minDayPrice;
    private double maxDayPrice;
    private double actualPrice;
    private String creationDate;
    @ToMany(referencedJoinProperty = "coinId")
    @OrderBy("date ASC")
    private List<Purchase> purchases;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1140916136)
    private transient CoinDao myDao;

    @Generated(hash = 1384492340)
    public Coin(Long id, String name, String coinName, String logoURL,
        double minDayPrice, double maxDayPrice, double actualPrice,
        String creationDate) {
        this.id = id;
        this.name = name;
        this.coinName = coinName;
        this.logoURL = logoURL;
        this.minDayPrice = minDayPrice;
        this.maxDayPrice = maxDayPrice;
        this.actualPrice = actualPrice;
        this.creationDate = creationDate;
    }

    @Generated(hash = 1629455692)
    public Coin() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoinName() {
        return this.coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getLogoURL() {
        return this.logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public double getMinDayPrice() {
        return this.minDayPrice;
    }

    public void setMinDayPrice(double minDayPrice) {
        this.minDayPrice = minDayPrice;
    }

    public double getMaxDayPrice() {
        return this.maxDayPrice;
    }

    public void setMaxDayPrice(double maxDayPrice) {
        this.maxDayPrice = maxDayPrice;
    }

    public double getActualPrice() {
        return this.actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1648273018)
    public List<Purchase> getPurchases() {
        if (purchases == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PurchaseDao targetDao = daoSession.getPurchaseDao();
            List<Purchase> purchasesNew = targetDao._queryCoin_Purchases(id);
            synchronized (this) {
                if (purchases == null) {
                    purchases = purchasesNew;
                }
            }
        }
        return purchases;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 528316983)
    public synchronized void resetPurchases() {
        purchases = null;
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
    @Generated(hash = 212404555)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCoinDao() : null;
    }

}