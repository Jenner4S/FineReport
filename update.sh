#! /bin/bash
svn up designer
svn up designer_base
svn up designer_chart
svn up designer_form
ftp -n<<!
open 192.168.100.98
user anonymous anonymous
binary
cd /8.0/CN
lcd lib
prompt
mget fr-core-8.0.jar fr-chart-8.0.jar fr-report-8.0.jar fr-platform-8.0.jar fr-performance-8.0.jar fr-third-8.0.jar 
close
bye
!
