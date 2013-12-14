JiraVersionsAutoSortPlugin
==========================

Jira provide great capabilities to manage project versions by just drag-and-drop:
https://confluence.atlassian.com/display/JIRA/Managing+Versions

But in our release cycle we build version each week. Moreover frequently for one project released versions from different
tags or branches like: 1.5.189, 1.3.68, 1.5.189.1, 1.3.69. Also such versions created in several projects (ESB, QUICK, REP).

For ordering and testing purposes (for example to perform search of task between versions for testing) we want order in
versions order, not in order it is added in time. Manual reordering is annoying and very expensive.

This plugin move new version automatically after corresponded version. F.e. 1.5.189.1 will be placed directly after 1.5.189.

Versions compared by split by dor (".") and then compare parts. If part is digits only, it compared as number. Otherwise
it compared as is by call compareTo on strings, so lexicographic order preserved. It is allow have sub-versions like:
1.5.350.mrg.1, 1.5.350.mrg.2 also ordered as desired.

It targeted now and tested for Jira 6.1 version.

Inspired by our (private) task: http://serverprog:1090/browse/IMUSCONFIGURE-197

If you add versions manually in administrative area you will need reload page to see new version order.

IntelliJ Idea project and maven pom provided.

Licensing
=========
Plugin distributed under terms of Apache license 2.0. Text copy of it you should get with package.

Building from source
====================
To build plugin from source you must install atlassian plugin SDK. Checkout source and then just run
```
atlas-mvn clean install
```

Installation
============
To install just got into administration in "Manage add-ons" section and upload resulting jar.

Feedback and bug reports
========================
Feedback are very welcome. Please fill bugs there, or contact me directly.