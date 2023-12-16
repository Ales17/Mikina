package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.entity.Guest;
import cz.geek.ubyport.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UbyportService {

    private final List<StatniPrislusnost> countryList = Arrays.asList(StatniPrislusnost.values());

    public ByteArrayOutputStream getUbyportStream(List<Guest> guests) throws IOException {
        UbyportUbytovatel ubytovatel = new UbyportUbytovatel();
        ubytovatel.setIdub("id1");
        ubytovatel.setZkratka("abbr1");
        ubytovatel.setUbytovatel("Apartmány u Mikiny");
        ubytovatel.setKontakt("Jan Sibelius, tel: 261 197 135");
        ubytovatel.setOkres("Praha 22");
        ubytovatel.setObec("Praha");
        ubytovatel.setCastObce("Hostivař");
        ubytovatel.setUlice("U Továren");
        ubytovatel.setCisloDomovni("538");
        ubytovatel.setCisloOrientacni("23a");
        ubytovatel.setPsc("11055");
        Ubyport ubyport = new Ubyport(ubytovatel);
        for (Guest g : guests) {
            UbyportUbytovany ubytovany = getUbyportUbytovany(g);
            ubyport.add(ubytovany);
        }
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ubyport.export(byteArray);
        return byteArray;
    }

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

    public List<StatniPrislusnost> getCountryList() {
        return countryList;
    }
}
