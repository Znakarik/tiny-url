mvn clean && mvn package
docker buildx build --platform linux/amd64,linux/arm64 -t znakarik/tiny-url:0.0.1 .
#docker run -p 8443:8443 znakarik/tiny-url -Dspring.profiles.active=prod
docker push znakarik/tiny-url:0.0.1