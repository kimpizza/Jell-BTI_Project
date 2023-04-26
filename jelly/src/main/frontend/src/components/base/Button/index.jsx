import styled from "styled-components";

const Button = styled.button`
    position: absolute;
    top:40%;
    left: 34%;
    width: 58%;
    height: 12%;
    background: #FFDD00;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    border-radius: 30px;
    border: 0;
    color: black;
    a{
        text-decoration: none;
        color : inherit;
        font-weight: bold;
        font-size: 30px;
        display: block;
    }
    &:hover{
        background-color: #f7d604;
    }
`

export default Button;