import Header from "@/components/domain/Header";
import Navigation from "@/components/domain/Navigation";
import Search from "@/components/domain/Search";
import SearchBox from "components/domain/SearchBox";
import styled from "styled-components";
import { useDispatch } from "react-redux";
import { asyncUpFetch } from "redux/jellyInfo";
import { useEffect } from "react";
import {
  MyPageBallonBlue,
  MyPageBallonGreen,
  MyPageBallonGreen_2,
  MyPageBallonRed,
  MyPageBallonRed_2,
  MyPageBallonSkyblue_2,
} from "pages/MyPage";
import Button from "components/base/Button";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { userInformationIn } from "redux/store";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;
const LoginBox = styled.div`
    position: absolute;
    right: 4%;
    top:4%;
`

const Home = () => {
  const dispatch = useDispatch();
  const navigate=useNavigate()
  let token = localStorage.getItem('token');
    
  //토큰이 있을 경우 유저정보 받아오기
  if(token){
    axios({
      method : "GET",
      url : `/oauth/login/userInfo?token=${token}`
  }).then((res)=>{
      // setUser(res.data.userInfo);
      dispatch(userInformationIn(res.data.userInfo));
      const userEmail = res.data.userInfo.mJelly; //mJelly를 mEmail or mNick으로 바꾸면 그걸로 받아온다.
      console.log(userEmail)
  })
  }else{
    console.log("로그인을 하지 않았습니다.")
  }
  

  useEffect(() => {
    dispatch(asyncUpFetch());
  }, []);

  return (
    <Container>
      <MyPageBallonBlue src="/balloonBlue.png" />
      <MyPageBallonRed src="/balloonRed.png" />
      <MyPageBallonRed_2 src="/balloonRed.png" />
      <MyPageBallonGreen src="/balloonGreen.png" />
      <MyPageBallonGreen_2 src="/balloonGreen.png" />
      <MyPageBallonSkyblue_2 src="/ballonSkyblue.png" />
      <LoginBox>
        <Button onClick={()=>navigate("/home/modal")} fontSize={30} fontWeight={700} padding={"0.6em 0.7em"} bgColor={"rgba(0, 163, 255, 0.2)"}>로그인하기</Button>
      </LoginBox>
      <Header />
      <Navigation />
      <Search />
      <SearchBox />
    </Container>
  );
};

export default Home;
