package searchengine.data;

import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.data.repository.LemmaEntityRepository;
import searchengine.model.LemmaEntity;
import searchengine.model.SiteEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LemmaEntityService {
    private final LemmaEntityRepository lemmaEntityRepository;

    public LemmaEntityService(LemmaEntityRepository lemmaEntityRepository) {
        this.lemmaEntityRepository = lemmaEntityRepository;
    }

    public LemmaEntity getLemmaBySite(String lemma, SiteEntity site) {
        return lemmaEntityRepository.getLemmaEntityByLemmaAndSite(lemma, site);
    }

    public void saveAll(Iterable<LemmaEntity> lemmas) {
        lemmaEntityRepository.saveAll(lemmas);
    }

    public List<LemmaEntity> getLemmaEntitiesBySite(SiteEntity site) {
        return lemmaEntityRepository.getLemmaEntitiesBySite(site);
    }

    public void deleteAllById(Iterable<? extends Integer> lemmas) {
        lemmaEntityRepository.deleteAllById(lemmas);
    }

    public LemmaEntity getByLemmaAndSite(String word, SiteEntity site) {
        LemmaEntity lemma = lemmaEntityRepository.getLemmaEntityByLemmaAndSite(word, site);
        if (lemma == null) {
            lemma = new LemmaEntity();
            lemma.setSite(site);
            lemma.setLemma(word);
            lemma.setFrequency(0);
        }
        int frequency = lemma.getFrequency();
        frequency++;
        lemma.setFrequency(frequency);

        lemmaEntityRepository.save(lemma);
        return lemma;
    }


    public List<LemmaEntity> saveLemmasFromList(HashMap<String, Integer> lemmasList, SiteEntity site) {
        List<LemmaEntity> newList = new ArrayList<>();
        lemmasList.forEach((key, value) -> {
            LemmaEntity lemma = getByLemmaAndSite(key, site);
            int frequency = lemma.getFrequency() + 1;
            lemma.setFrequency(frequency);
            newList.add(lemma);
        });
        saveAll(newList);
        return newList;
    }

}