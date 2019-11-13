package com.josedlpozo.galileo.realm

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.josedlpozo.galileo.realm.realmbrowser.files.model.RealmFile
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmSnapshooterTest {

    private val realmSnapshooter = RealmSnapshooter()

    @Test
    fun given_realm_databases_when_making_a_snapshoot_then_snapshoot_is_ok() {
        InstrumentationRegistry.getTargetContext().filesDir.deleteRecursively()
        Realm.init(InstrumentationRegistry.getTargetContext())
        val builder = RealmConfiguration.Builder()
        with(Realm.getInstance(builder.name("database_developers").deleteRealmIfMigrationNeeded().build())) {
            executeTransaction {
                developers.forEach { copyToRealmOrUpdate(it) }
            }
            close()
        }
        with(Realm.getInstance(builder.name("database_teams").deleteRealmIfMigrationNeeded().build())) {
            executeTransaction {
                teams.forEach { copyToRealmOrUpdate(it) }
            }
            close()
        }

        val snapshoot = realmSnapshooter.shoot(listOf(RealmFile("database_developers", "0", 0L),
                RealmFile("database_teams", "0", 0L)))

        val expected = "There are 2 Realm databases used by this app\n" +
                "\n" +
                "Database database_developers has 2 tables\n" +
                "- TeamModel has 2 columns and 0 rows\n" +
                "| id || name |\n" +
                "- DeveloperModel has 2 columns and 6 rows\n" +
                "| id || name |\n" +
                "0 fpulido \n" +
                "1 jmdelpozo \n" +
                "2 vfrancisco \n" +
                "3 jaznar \n" +
                "4 molmedo \n" +
                "5 efau \n" +
                "\n" +
                "\n" +
                "Database database_teams has 2 tables\n" +
                "- TeamModel has 2 columns and 2 rows\n" +
                "| id || name |\n" +
                "0 android \n" +
                "1 ios \n" +
                "- DeveloperModel has 2 columns and 0 rows\n" +
                "| id || name |\n" +
                "\n" +
                "\n"

        assertEquals(expected, snapshoot)
    }

}