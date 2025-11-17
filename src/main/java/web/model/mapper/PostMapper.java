package web.model.mapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// MyBatis Mapper 인터페이스
// @Mapper 어노테이션을 통해 Spring이 이 인터페이스를 구현하고 Bean으로 등록합니다.
@Mapper
public interface PostMapper {

    /**
     * 새 게시글을 데이터베이스에 저장합니다.
     * 게시글 저장 후, MyBatis가 Post 객체에 자동 생성된 ID를 채워줍니다.
     */
    void insertPost(Post post);

    /**
     * 모든 게시글 목록을 조회합니다.
     */
    List<Post> findAllPosts();


}
