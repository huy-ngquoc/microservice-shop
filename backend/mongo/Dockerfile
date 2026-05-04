FROM mongo:8

COPY keyfile/mongo-keyfile /etc/mongo/keyfile

RUN chown mongodb:mongodb /etc/mongo/keyfile \
    && chmod 400 /etc/mongo/keyfile
