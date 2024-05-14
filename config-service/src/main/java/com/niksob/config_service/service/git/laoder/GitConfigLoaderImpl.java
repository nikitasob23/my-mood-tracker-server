package com.niksob.config_service.service.git.laoder;

import com.niksob.config_service.config.git.GitRemoteRepoConfig;
import com.niksob.config_service.util.file.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
@Profile("prod")
@AllArgsConstructor
@Slf4j
public class GitConfigLoaderImpl implements GitConfigLoader {
    private final FileUtil fileUtil;

    private final GitRemoteRepoConfig gitRemoteRepoConfig;
    @Value("${repo.local.git.path}")
    private String localRepoPath;

    @Autowired
    public GitConfigLoaderImpl(
            FileUtil fileUtil,
            GitRemoteRepoConfig gitRemoteRepoConfig
    ) {
        this.fileUtil = fileUtil;
        this.gitRemoteRepoConfig = gitRemoteRepoConfig;
    }

    @Override
    public void load() {
        log.info("Start to clone remote git repository: %s by user: %s"
                .formatted(gitRemoteRepoConfig.getUri(), gitRemoteRepoConfig.getUsername())
        );
        try {
            fileUtil.dropDir(localRepoPath);

            var credentialsProvider = new UsernamePasswordCredentialsProvider(
                    gitRemoteRepoConfig.getUsername(), gitRemoteRepoConfig.getPassword()
            );
            Git.cloneRepository()
                    .setURI(gitRemoteRepoConfig.getUri())
                    .setDirectory(Paths.get(localRepoPath).toFile())
                    .setCredentialsProvider(credentialsProvider).call();
        } catch (GitAPIException e) {
            log.error("Failed to clone remote git repository", e);
        }
        log.info("Git repository was cloned into local dir: " + localRepoPath);
    }
}
