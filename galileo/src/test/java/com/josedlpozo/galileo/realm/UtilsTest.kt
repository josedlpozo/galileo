package com.josedlpozo.galileo.realm

import com.josedlpozo.galileo.realm.browser.helper.Utils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import org.junit.Test

class UtilsTest {

    @Test
    @Throws(NoSuchFieldException::class)
    fun createParametrizedNameForListField() {
        val f = RealmTestModel::class.java.getDeclaredField("aStringList")
        assertEquals("List<String>", Utils.createParametrizedName(f))
    }

    @Test
    fun createBlobValueStringForNull() {
        assertNull(Utils.createBlobValueString(null, 0))
    }

    @Test
    fun createBlobValueStringForEmptyBlob() {
        assertEquals("byte[] = {}", Utils.createBlobValueString(byteArrayOf(), 0))
    }

    @Test
    fun createBlobValueStringForSingleEntryBlob() {
        assertEquals("byte[] = {1}", Utils.createBlobValueString(byteArrayOf(1), 0))
    }

    @Test
    fun createBlobValueStringForBlob() {
        assertEquals("byte[] = {1, 2, 3, 4, 5, 6, 7, 8, 9}", Utils.createBlobValueString(byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9), 0))
    }

    @Test
    fun createBlobValueStringForBlobLimited() {
        assertEquals("byte[] = {1, 2, 3, ...}", Utils.createBlobValueString(byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9), 3))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isNumber() {
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertTrue(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isNumber(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isInteger() {
        assertTrue(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertTrue(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isInteger(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isLong() {
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertTrue(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertTrue(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isLong(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isShort() {
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertTrue(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertTrue(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isShort(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isByte() {
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertTrue(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertTrue(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isByte(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isFloat() {
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertTrue(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertTrue(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isFloat(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isDouble() {
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertTrue(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertTrue(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isDouble(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isBoolean() {
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertTrue(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertTrue(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isBoolean(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isString() {
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertTrue(Utils.isString(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isString(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isBlob() {
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertTrue(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isBlob(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isDate() {
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertTrue(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isDate(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isParametrizedField() {
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertTrue(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aString")))
        assertTrue(Utils.isParametrizedField(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }

    @Test
    @Throws(NoSuchFieldException::class)
    fun isRealmObjectField() {
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("anInteger")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBoxedInteger")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aLong")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBoxedLong")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aShort")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBoxedShort")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aByte")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBoxedByte")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aDouble")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBoxedDouble")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aFloat")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBoxedFloat")))

        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBool")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBoxedBool")))

        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aBlob")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aDate")))
        assertTrue(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("anObject")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aRealmList")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aString")))
        assertFalse(Utils.isRealmObjectField(RealmTestModel::class.java.getDeclaredField("aStringList")))
    }
}