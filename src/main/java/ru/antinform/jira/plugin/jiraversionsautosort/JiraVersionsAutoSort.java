package ru.antinform.jira.plugin.jiraversionsautosort;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.project.VersionCreateEvent;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JiraVersionsAutoSort extends AbstractIssueEventListener implements InitializingBean, DisposableBean{
	private static final Logger log = LoggerFactory.getLogger(JiraVersionsAutoSort.class);

	private final VersionManager versionManager;
	private final EventPublisher eventPublisher;

	public JiraVersionsAutoSort(EventPublisher eventPublisher, VersionManager versionManager)
	{
		this.eventPublisher = eventPublisher;
		this.versionManager = versionManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		eventPublisher.register(this);
	}

	@Override
	public void destroy() throws Exception
	{
		eventPublisher.unregister(this);
	}

	@EventListener
	public void onVersionCreated(VersionCreateEvent versionCreatedEvent)
	{
		Version version = versionManager.getVersion(versionCreatedEvent.getVersionId());
		log.info("Created version:: " + version.getName());

		List<Version> sorted = versionManager.getVersions(version.getProjectObject());
		Collections.sort(
			(List) sorted
			,new Comparator(){
				@Override
				public int compare(Object o1, Object o2) {
					Version v1 = (Version)o1;
					Version v2 = (Version)o2;
					String[] l1 = v1.getName().split("\\.");
					String[] l2 = v2.getName().split("\\.");
					Integer i1 = null, i2 = null;
					String s1 = null, s2 = null;
					for (int i = 0; i < Math.max(l1.length, l2.length); i++){
						if (i == l1.length) return -1;
						if (i == l2.length) return 1;
						try{
							i1 = Integer.parseInt(l1[i]);
						}
						catch(RuntimeException re){
							s1 = l1[i];
						}
						try{
							i2 = Integer.parseInt(l2[i]);
						}
						catch(RuntimeException re){
							s2 = l2[i];
						}
						if (null != i1 && null != i2){
							int res = i1.compareTo(i2);
							if (0 != res) return res;
						}
						if (null != s1 && null != s2){ // Strings compare as is
							int res = s1.compareTo(s2);
							if (0 != res) return res;
						}
					}
					return 0;
				}
			}
		);

		int i = -1;
		while(!version.getName().equals(sorted.get(++i).getName())){ } // just search
		versionManager.moveVersionAfter(version, sorted.get(i-1).getId());
		log.info("JiraVersionsAutoSort:: Move version [" + version.getName() + "](id=" + version.getId() + ") after [" + sorted.get(i-1).getName() + "](id=" + sorted.get(i-1).getId() + ')');
	}
}