package lawfirm.project.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	boolean store(MultipartFile file,String caseID);

	Stream<Path> loadAll(String caseID);

	Path load(String filename);

	Resource loadAsResource(String filename, String folderNo);

	void deleteAll();

}
