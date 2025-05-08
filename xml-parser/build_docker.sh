#!/bin/bash
docker build -f app.Dockerfile -t uoles/xml-parser:1.0.1 .

docker run -d --name xml-parser \
	  --publish 8090:8090 \
    uoles/xml-parser:1.0.1