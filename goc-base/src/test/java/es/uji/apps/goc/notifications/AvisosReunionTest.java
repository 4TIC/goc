package es.uji.apps.goc.notifications;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import es.uji.apps.goc.builders.ReunionBuilder;
import es.uji.apps.goc.dto.Reunion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class AvisosReunionTest {

    @Test
    public void testCreaICalCreatesIcalCorrectly() {
        String calendar = null;
        ReunionBuilder reunionBuilder = new ReunionBuilder();
        Reunion reunionDeTest =
            reunionBuilder.withId(11l).withAsunto("asunto de prueba").withCreadorEmail("miguel@email.com")
                .withCreadorNombre("miguel").withDuracion(150l).withFecha(new Date()).withUbicacion("en la uji")
                .build();
        Class<AvisosReunion> clazz = AvisosReunion.class;
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("creaICal", Reunion.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            calendar = (String) method.invoke(new AvisosReunion(null, null, null, null), reunionDeTest);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        assertThat(calendar, is(notNullValue()));
    }

    @Test
    public void testCreaIcalCreatesIcalCorrectlyWithoutOrganizer() {
        String calendar = null;
        ReunionBuilder reunionBuilder = new ReunionBuilder();
        Reunion reunionDeTest =
            reunionBuilder.withId(11l).withAsunto("asunto de prueba").withDuracion(150l).withFecha(new Date())
                .withUbicacion("en la uji").build();
        Class<AvisosReunion> clazz = AvisosReunion.class;
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("creaICal", Reunion.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            calendar = (String) method.invoke(new AvisosReunion(null, null, null, null), reunionDeTest);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        assertThat(calendar, is(notNullValue()));
    }

    @Test
    public void testCreaIcalCreatesIcalCorrectlyWithoutData() {
        String calendar = null;
        ReunionBuilder reunionBuilder = new ReunionBuilder();
        Reunion reunionDeTest =
            reunionBuilder.build();
        Class<AvisosReunion> clazz = AvisosReunion.class;
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("creaICal", Reunion.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            calendar = (String) method.invoke(new AvisosReunion(null, null, null, null), reunionDeTest);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        assertThat(calendar, is(notNullValue()));
    }
}
