package be.bagofwords.db.helper;

import be.bagofwords.db.DataInterfaceFactory;
import be.bagofwords.application.annotations.BowComponent;
import be.bagofwords.application.file.OpenFilesManager;
import be.bagofwords.application.memory.MemoryManager;
import be.bagofwords.cache.CachesManager;
import be.bagofwords.db.DatabaseBackendType;
import be.bagofwords.db.filedb.FileDataInterfaceFactory;
import be.bagofwords.db.kyoto.KyotoDataInterfaceFactory;
import be.bagofwords.db.leveldb.LevelDBDataInterfaceFactory;
import be.bagofwords.db.lmdb.LMDBDataInterfaceFactory;
import be.bagofwords.db.memory.InMemoryDataInterfaceFactory;
import be.bagofwords.db.remote.RemoteDatabaseInterfaceFactory;
import be.bagofwords.db.rocksdb.RocksDBDataInterfaceFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 9/4/14.
 */

@BowComponent
public class DataInterfaceFactoryFactory {

    @Autowired
    private CachesManager cachesManager;
    @Autowired
    private MemoryManager memoryManager;
    @Autowired
    private OpenFilesManager openFilesManager;
    @Autowired
    private UnitTestEnvironmentProperties environmentProperties;

    public DataInterfaceFactory createFactory(DatabaseBackendType backendType) {
        switch (backendType) {
            case FILE:
                return new FileDataInterfaceFactory(openFilesManager, cachesManager, memoryManager, environmentProperties.getDataDirectory() + "server/");
            case REMOTE:
                return new RemoteDatabaseInterfaceFactory(cachesManager, memoryManager, environmentProperties.getDatabaseServerAddress(), environmentProperties.getDataInterfaceServerPort());
            case MEMORY:
                return new InMemoryDataInterfaceFactory(cachesManager, memoryManager);
            case LEVELDB:
                return new LevelDBDataInterfaceFactory(cachesManager, memoryManager, environmentProperties.getDataDirectory() + "leveLDB/");
            case LMDB:
                return new LMDBDataInterfaceFactory(cachesManager, memoryManager, environmentProperties.getDataDirectory() + "lmDB/");
            case KYOTO:
                return new KyotoDataInterfaceFactory(cachesManager, memoryManager, environmentProperties.getDataDirectory() + "kyotoDB/");
            case ROCKSDB:
                return new RocksDBDataInterfaceFactory(cachesManager, memoryManager, environmentProperties.getDataDirectory() + "rocksDB/", false);
            case ROCKSDB_PATCHED:
                return new RocksDBDataInterfaceFactory(cachesManager, memoryManager, environmentProperties.getDataDirectory() + "rocksDB/", true);
            default:
                throw new RuntimeException("Unknown backend type " + backendType);
        }
    }

}