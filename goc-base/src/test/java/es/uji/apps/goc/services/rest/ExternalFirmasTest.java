package es.uji.apps.goc.services.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import es.uji.apps.goc.dto.*;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import es.uji.apps.goc.model.Cargo;

import static es.uji.commons.testing.hamcrest.ClientNoContentResponseMatcher.noContentClientResponse;
import static org.junit.Assert.assertThat;

public class ExternalFirmasTest extends JerseySpringTest
{
    @Test
    public void envioReunionAFirmar() throws Exception
    {
        ReunionFirma reunionFirma = getReunionExterna();

        ClientResponse response = this.resource.path(firmasUrl).type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken).post(ClientResponse.class, reunionFirma);

        assertThat(response, noContentClientResponse());
    }

    private ReunionFirma getReunionExterna()
    {
        ReunionFirma reunionFirma = new ReunionFirma();
        reunionFirma.setId(1L);
        reunionFirma.setAsunto("Prueba Reunión Externa");
        reunionFirma.setFecha(new Date());
        reunionFirma.setNumeroSesion(5L);
        reunionFirma.setDuracion(60L);
        reunionFirma.setUbicacion("Ubicacion pruebas");
        reunionFirma.setAcuerdos(("Acuerdos Test"));
        reunionFirma.setUrlGrabacion("http://uji.es/proves.mp3");
        reunionFirma.setTelematica(true);
        reunionFirma.setTelematicaDescripcion("Reunión telemática, y estas son las instrucciones de conexión");

        List<PuntoOrdenDiaFirma> listaPuntosOrdenDia = new ArrayList<>();
        PuntoOrdenDiaFirma p1 = new PuntoOrdenDiaFirma();
        p1.setId(1L);
        p1.setTitulo("Punto Orden Dia 1");
        p1.setAcuerdos("Acuerdos Punto Orden Dia 1");
        p1.setDeliberaciones("Deliberaciones Punto Orden Dia 1");
        p1.setDescripcion("Descripción Punto Orden Día 1");
        p1.setOrden(10L);
        PuntoOrdenDiaFirma p2 = new PuntoOrdenDiaFirma();
        p2.setId(2L);
        p2.setTitulo("Punto Orden Dia 2");
        p2.setAcuerdos("Acuerdos Punto Orden Dia 2");
        p2.setDeliberaciones("Deliberaciones Punto Orden Dia 2");
        p2.setDescripcion("Descripción Punto Orden Día 2");
        p2.setOrden(20L);

        listaPuntosOrdenDia.add(p1);
        listaPuntosOrdenDia.add(p2);

        reunionFirma.setPuntosOrdenDia(listaPuntosOrdenDia);

        List<OrganoFirma> organos = new ArrayList<>();
        List<MiembroFirma> asistentes = new ArrayList<>();

        Cargo c1 = new Cargo();
        c1.setId("1");
        c1.setNombre("Presidente");
        Cargo c2 = new Cargo();
        c2.setId("2");
        c2.setNombre("Vocal");

        MiembroFirma m1 = new MiembroFirma();
        m1.setId("1");
        m1.setNombre("Miembro Externo 1 Test");
        m1.setEmail("miembro1@example.com");
        m1.setCargo(c1);
        MiembroFirma m2 = new MiembroFirma();
        m2.setId("2");
        m2.setNombre("Miembro Externo 2 Test");
        m2.setEmail("miembro2@example.com");
        m2.setCargo(c2);

        MiembroFirma m3 = new MiembroFirma();
        m3.setId("3");
        m3.setNombre("Miembro Externo 3 Test");
        m3.setEmail("miembro3@example.com");
        m3.setCargo(c1);

        OrganoFirma o1 = new OrganoFirma();
        o1.setId("1");
        o1.setNombre("Organo 1 Test");
        o1.setTipoOrganoId(1L);
        o1.setTipoCodigo("1");
        o1.setTipoNombre("Departamento");

        List<MiembroFirma> asistentes1 = new ArrayList<>();
        asistentes1.add(m1);
        asistentes1.add(m2);
        o1.setAsistentes(asistentes1);

        OrganoFirma o2 = new OrganoFirma();
        o2.setId("2");
        o2.setNombre("Organo 2 Test");
        o2.setTipoOrganoId(2L);
        o2.setTipoCodigo("2");
        o2.setTipoNombre("Unidad de Gestión");

        List<MiembroFirma> asistentes2 = new ArrayList<>();
        asistentes2.add(m1);
        asistentes2.add(m2);
        o1.setAsistentes(asistentes2);

        reunionFirma.setOrganos(organos);

        List<InvitadoFirma> invitadoFirmas = new ArrayList<>();
        InvitadoFirma invitado = new InvitadoFirma();
        invitado.setId(1L);
        invitado.setNombre("Invitado 1");
        invitado.setEmail("invitado@gmail.com");

        reunionFirma.setInvitados(invitadoFirmas);

        return reunionFirma;
    }
}
