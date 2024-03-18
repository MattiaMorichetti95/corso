package com.sistemi.informativi.service;

import com.sistemi.informativi.entity.Academy;
import com.sistemi.informativi.exception.CustomException;
import com.sistemi.informativi.repository.AcademyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AcademyServiceImpl implements AcademyService{

    @Value("${no.academies}")
    private String noAcademies;

    @Value("${no.academy}")
    private String noAcademy;

    private AcademyRepository academyRepository;

    public AcademyServiceImpl(AcademyRepository academyRepository) {
        this.academyRepository = academyRepository;
    }

    /*
    All'interno di una Applicazione Spring Boot in cui si implementa un Restful Service Provider,
    è pratica consolidata far restituire ai metodi dello strato Service degli oggetti Java in
    maniera tale che il RestController che invocherà i metodi del Service avrà già a disposizione
    tali oggetti che verranno successivamente convertiti dall'Object Mapper di Spring Boot
    in formato JSON
     */



    @Override
    public Academy checkSaveOrUpdate(Academy academy) {

        Academy savedOrUpdated = null;

        try {

            savedOrUpdated=academyRepository.save(academy);
        }
        catch (IllegalArgumentException | OptimisticLockingFailureException ex){
            ex.printStackTrace();
        }

        return savedOrUpdated;
    }

    @Override
    public List<Academy> checkFindAllAcademy() throws CustomException {

        List<Academy>academies = academyRepository.findAll();

        if(academies.isEmpty()) throw new CustomException(noAcademies);

        return academies;
    }

    @Override
    public Academy checkFindAcademyByCode(String code) throws CustomException {

        /*
        Il metodo findById viene fornito da Spring Data JPA e consente di restiruire un oggetto
        Java che rappresenta il contenuto informativo relativo ad un record di un database con una
        specifica chiave primaria passata in input al metodo

        findById a partire dalla versione 2.7 di Spring boot è un metodo funzionale, possiamo infatti
        invocare con l'operatore . una funziona di nome orElseThrow alla quale possiamo passare in input una
        eccezione

        Nel caso in cui l'operazione non vada in eccezione il metodo findById restituisce un oggetto,
        altrimenti viene catturata l'eccezione che richiamiamo nella funzione orElseThrow
         */
        return academyRepository.findById(code).orElseThrow(()->new CustomException(noAcademy));
    }

    @Override
    public Map<String, Boolean> checkRemoveAcademy(String code) {

        Map<String,Boolean> removeMap = new HashMap<>();

        try{
            academyRepository.deleteById(code);
            removeMap.put("deletion",true);
        } catch (IllegalArgumentException ex){
            removeMap.put("deletion",false);
            ex.printStackTrace();
        }

        return removeMap;
    }
}
