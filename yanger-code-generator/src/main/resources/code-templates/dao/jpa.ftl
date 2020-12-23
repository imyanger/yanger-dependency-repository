package ${daoInterfacePackage};

import ${modelPackage}.${modelName};

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description ${modelName}的数据持久层Dao类接口类
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
public interface ${daoInterfaceName} extends JpaRepository<${modelName}, ${pkFieldClass}> {

}