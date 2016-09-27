package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import es.uji.apps.goc.dao.OrganoReunionDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.model.OrganoLocal;

@Service
@Component
public class OrganoReunionService
{
    @Autowired
    private OrganoReunionDAO organoReunionDAO;

    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    public OrganoReunion addOrganoReunion(Long reunionId, String organoId, Boolean externo, Long connectedUserId) {
        OrganoReunion organoReunion = new OrganoReunion();

        if (externo) {
            organoReunion.setOrganoExternoId(organoId);
        } else {
            OrganoLocal organoLocal = new OrganoLocal();
            organoLocal.setId(Long.parseLong(organoId));
            organoReunion.setOrganoLocal(organoLocal);
        }

        Reunion reunion = new Reunion();
        reunion.setId(reunionId);
        organoReunion.setReunion(reunion);

        return organoReunionDAO.insert(organoReunion);
    }
}
