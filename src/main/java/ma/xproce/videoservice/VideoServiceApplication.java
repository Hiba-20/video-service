package ma.xproce.videoservice;

import ma.xproce.videoservice.entities.Creator;
import ma.xproce.videoservice.entities.Video;
import ma.xproce.videoservice.repositories.CreatorRepository;
import ma.xproce.videoservice.repositories.VideoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class VideoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CreatorRepository creatorRepository, VideoRepository videoRepository) {
        return args -> {
            List<Creator> creators = List.of(
                    Creator.builder().name("John Doe").email("john@example.com").build(),
                    Creator.builder().name("Alice Smith").email("alice@example.com").build()
            );
            creatorRepository.saveAll(creators);

            List<Video> videos = List.of(
                    Video.builder().name("Spring Boot Tutorial")
                            .url("http://example.com/video1")
                            .description("Learn Spring Boot")
                            .datePublication(new Date())
                            .creator(creators.get(0)).build(),
                    Video.builder().name("GraphQL Basics")
                            .url("http://example.com/video2")
                            .description("Learn GraphQL")
                            .datePublication(new Date())
                            .creator(creators.get(1)).build()
            );
            videoRepository.saveAll(videos);
        };

    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}