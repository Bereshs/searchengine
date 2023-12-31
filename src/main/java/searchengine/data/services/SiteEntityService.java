package searchengine.data.services;

import org.springframework.stereotype.Service;
import searchengine.data.repository.SiteEntityRepository;
import searchengine.data.services.html.HtmlDocument;
import searchengine.model.SiteEntity;
import searchengine.model.SiteStatus;

import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SiteEntityService {
    private final SiteEntityRepository siteEntityRepository;

    public SiteEntityService(SiteEntityRepository siteEntityRepository) {
        this.siteEntityRepository = siteEntityRepository;
    }

    public SiteEntity getByUrl(String url) {
        return siteEntityRepository.getByUrl(getRootPath(url));
    }
    public SiteEntity getByUrlOrCreate(String url) {
        SiteEntity siteEntity = siteEntityRepository.getByUrl(getRootPath(url));
        if (siteEntity != null) {
            return siteEntity;
        }
        siteEntity = new SiteEntity();
        siteEntity.setUrl(getRootPath(url));
        siteEntity.setName("noname");
        siteEntity.setStatus(SiteStatus.INDEXING);
        save(siteEntity);

        siteEntity = siteEntityRepository.getByUrl(getRootPath(url));
        return siteEntity;
    }

    public SiteEntity getByDocument(HtmlDocument document) {
        SiteEntity siteEntity = siteEntityRepository.getByUrl(getRootPath(document.getRootPath()));
        if (siteEntity == null) {
            siteEntity = new SiteEntity();
            siteEntity.setUrl(document.getRootPath().substring(0, document.getRootPath().length() - 1));
            siteEntity.setName(document.getTitle());
            siteEntity.setStatus(SiteStatus.INDEXING);
        }
        siteEntity.setName(document.getTitle());
        save(siteEntity);

        return siteEntity;
    }

    public void save(SiteEntity site) {
        site.setStatusTime(LocalDateTime.now());
        siteEntityRepository.save(site);
    }

    public void deleteById(int id) {
        siteEntityRepository.deleteById(id);
    }

    public String getRootPath(String path) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }

        Pattern pattern = Pattern.compile("(https?://.*?/)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String result = matcher.group(0);
            return result.substring(0, result.length() - 1);
        }
        return null;
    }

    public String getRelativePath(String path) {
        String rootPath=getRootPath(path);
        return path.substring(rootPath.length());
    }

    public void saveStatusSite(SiteEntity site, SiteStatus siteStatus) {
        if (!site.getStatus().equals(siteStatus)) {
            site.setStatus(siteStatus);
            save(site);
        }
    }

    public void saveSiteError(String url, String lastError) {
        SiteEntity site = siteEntityRepository.getByUrl(url);
        site.setLastError(lastError);
        siteEntityRepository.save(site);
    }

  }
