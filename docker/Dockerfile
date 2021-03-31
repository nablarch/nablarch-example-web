FROM jboss/wildfly:23.0.0.Final
USER root
RUN localedef -v -c -i ja_JP -f UTF-8 ja_JP.UTF-8; echo "";
env LANG=ja_JP.UTF-8
RUN rm -f /etc/localtime
RUN ln -fs /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

USER jboss
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]