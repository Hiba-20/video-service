package ma.xproce.videoservice.web;

import ma.xproce.videoservice.dtos.CreatorDTO;
import ma.xproce.videoservice.dtos.CreatorRequest;
import ma.xproce.videoservice.dtos.VideoDTO;
import ma.xproce.videoservice.dtos.VideoRequest;
import ma.xproce.videoservice.entities.Creator;
import ma.xproce.videoservice.entities.Video;
import ma.xproce.videoservice.repositories.CreatorRepository;
import ma.xproce.videoservice.repositories.VideoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Controller
public class VideoGraphQLController {

    private final CreatorRepository creatorRepository;
    private final VideoRepository videoRepository;
    private final ModelMapper modelMapper;

    public VideoGraphQLController(CreatorRepository creatorRepository, VideoRepository videoRepository, ModelMapper modelMapper) {
        this.creatorRepository = creatorRepository;
        this.videoRepository = videoRepository;
        this.modelMapper = modelMapper;
    }

    @QueryMapping
    public List<Video> videoList() {
        return videoRepository.findAll();
    }

    @QueryMapping
    public Creator creatorById(@Argument Long id) {
        return creatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Creator %s not found", id)));
    }

    @MutationMapping
    public CreatorDTO saveCreator(@Argument CreatorRequest creator) {
        Creator creatorEntity = modelMapper.map(creator, Creator.class);

        Creator savedCreator = creatorRepository.save(creatorEntity);

        return modelMapper.map(savedCreator, CreatorDTO.class);
    }

    @MutationMapping
    public VideoDTO saveVideo(@Argument VideoRequest video) {
        // Mapping DTO Request -> Entité JPA
        Video videoEntity = modelMapper.map(video, Video.class);

        if (videoEntity.getCreator() != null) {
            creatorRepository.save(videoEntity.getCreator());
        }

        Video savedVideo = videoRepository.save(videoEntity);

        // Mapping Entité JPA -> DTO Response
        return modelMapper.map(savedVideo, VideoDTO.class);
    }

    @SubscriptionMapping
    public Flux<Video> notifyVideoChange() {
        return Flux.fromStream(
                Stream.generate(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Simuler une mise à jour d'un Creator et d'une Video
                    CreatorRequest creatorRequest = CreatorRequest.builder()
                            .name("x" + new Random().nextInt())
                            .email("x@gmail.com")
                            .build();

                    // Adapter selon vos services/repositories existants
                    Creator creator = creatorRepository.save(
                            modelMapper.map(creatorRequest, Creator.class)
                    );

                    Video video = videoRepository.findById(1L).orElse(new Video());
                    video.setCreator(creator);
                    return videoRepository.save(video);
                })
        );
    }
}