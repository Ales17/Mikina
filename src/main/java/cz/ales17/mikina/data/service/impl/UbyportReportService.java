package cz.ales17.mikina.data.service.impl;

import cz.ales17.mikina.data.model.Company;
import cz.ales17.mikina.data.model.Guest;
import cz.ales17.mikina.data.service.ReportService;
import cz.geek.ubyport.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Getter
@Service
public class UbyportReportService implements ReportService {

    private final List<StatniPrislusnost> countryList = Arrays.asList(StatniPrislusnost.values());

    private UbyportUbytovany getUbyportUbytovany(Guest g) {
        UbyportUbytovany ubytovany = new UbyportUbytovany();
        ubytovany.setUbytovaniOd(g.getDateArrived());
        ubytovany.setUbytovaniDo(g.getDateLeft());
        ubytovany.setPrijmeni(g.getLastName());
        ubytovany.setJmeno(g.getFirstName());
        ubytovany.setNarozeni(g.getBirthDate());
        ubytovany.setStatniPrislusnost(g.getNationality());
        ubytovany.setBydliste(g.getAddress());
        ubytovany.setDoklad(g.getIdNumber());
        ubytovany.setViza("");
        ubytovany.setUcelPobytu(UcelPobytu.U10);
        ubytovany.setPoznamka("");
        return ubytovany;
    }

    @Override
    public byte[] getReportBytes(Company c, List<Guest> guests) throws Exception {
        UbyportUbytovatel ubytovatel = new UbyportUbytovatel();
        ubytovatel.setIdub(c.getUbyportId());
        ubytovatel.setZkratka(c.getUbyportAbbr());
        ubytovatel.setUbytovatel(c.getName());
        ubytovatel.setKontakt(c.getUbyportContact());
        ubytovatel.setOkres(c.getDistrict());
        ubytovatel.setObec(c.getMunicipality());
        ubytovatel.setCastObce(c.getMunicipalityQuarter());
        ubytovatel.setUlice(c.getStreet());
        ubytovatel.setCisloDomovni(c.getHouseNumber());
        ubytovatel.setCisloOrientacni(c.getRegistrationNumber());
        ubytovatel.setPsc(c.getZipCode());
        Ubyport ubyport = new Ubyport(ubytovatel);
        for (Guest g : guests) {
            if (g.getDateArrived() != null && g.getDateLeft() != null) {
                UbyportUbytovany ubytovany = getUbyportUbytovany(g);
                ubyport.add(ubytovany);
            }
        }
        return ubyport.export();
    }
}
