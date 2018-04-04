package com.nafaaazaiez.cryptofolder.db;


import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * Created by Ussef on 20/01/2018.
 */
@Entity(
    // Flag to make an entity "active": Active entities have update,
    // delete, and refresh methods.
    active = true,

    // Specifies the name of the table in the database.
    // By default, the name is based on the entities class name.
    nameInDb = "CRYPTO_PURCHASE",

    // Whether an all properties constructor should be generated.
    // A no-args constructor is always required.
    generateConstructors = true,

    // Whether getters and setters for properties should be generated if missing.
    generateGettersSetters = true
)
public class Purchase {

    @Id(autoincrement = true)
    private Long id;
    private double amount;
    private double unitPrice;
    private String date;
    private String platform;
    private String currency;
    private Long coinId;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1807587041)
    private transient PurchaseDao myDao;

    @Generated(hash = 1898397634)
    public Purchase(Long id, double amount, double unitPrice, String date, String platform,
        String currency, Long coinId) {
        this.id = id;
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.date = date;
        this.platform = platform;
        this.currency = currency;
        this.coinId = coinId;
    }

    @Generated(hash = 1281646125)
    public Purchase() {
    }

    public double getTotal() {
        return this.amount * this.unitPrice;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Long getCoinId() {
        return this.coinId;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
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

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 219300322)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPurchaseDao() : null;
    }

}

