/*
 * This file is generated by jOOQ.
 */
package jp.quotest.quocdt.db.tables.records;


import jp.quotest.quocdt.db.tables.Books;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class BooksRecord extends UpdatableRecordImpl<BooksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.books.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.books.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.books.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.books.title</code>.
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.books.price</code>.
     */
    public void setPrice(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.books.price</code>.
     */
    public Long getPrice() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.books.publication_status</code>.
     */
    public void setPublicationStatus(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.books.publication_status</code>.
     */
    public Boolean getPublicationStatus() {
        return (Boolean) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BooksRecord
     */
    public BooksRecord() {
        super(Books.BOOKS);
    }

    /**
     * Create a detached, initialised BooksRecord
     */
    public BooksRecord(Integer id, String title, Long price, Boolean publicationStatus) {
        super(Books.BOOKS);

        setId(id);
        setTitle(title);
        setPrice(price);
        setPublicationStatus(publicationStatus);
        resetChangedOnNotNull();
    }
}
