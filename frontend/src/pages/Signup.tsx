import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/Signup.css';

enum SignupStep {
  EMAIL_INPUT = 0,
  TOKEN_VERIFICATION = 1,
  USER_REGISTRATION = 2,
}

const Signup: React.FC = () => {
  // 공통 상태
  const [email, setEmail] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [currentStep, setCurrentStep] = useState<SignupStep>(SignupStep.EMAIL_INPUT);
  
  // 이메일 검증 관련 상태
  const [emailSent, setEmailSent] = useState(false);
  const [verificationToken, setVerificationToken] = useState('');
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  
  // 회원 정보 관련 상태
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  
  const { register, sendVerificationEmail, verifyEmail, loading, error: authError } = useAuth();
  const navigate = useNavigate();

  // AuthContext의 에러 메시지를 로컬 상태에 반영
  useEffect(() => {
    if (authError) {
      setError(authError);
    }
  }, [authError]);

  // 이메일 인증 코드 전송
  const handleSendVerificationEmail = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    
    try {
      console.log('이메일 인증 요청 전송:', email);
      const success = await sendVerificationEmail(email);
      if (success) {
        setEmailSent(true);
        setCurrentStep(SignupStep.TOKEN_VERIFICATION);
      }
    } catch (err) {
      setError('이메일 전송에 실패했습니다. 다시 시도해주세요.');
      console.error('Email sending error:', err);
    }
  };
  
  // 이메일 인증 코드 확인
  const handleVerifyToken = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    
    try {
      const response = await verifyEmail(email, verificationToken);
      if (response.verified) {
        setIsEmailVerified(true);
        setCurrentStep(SignupStep.USER_REGISTRATION);
      } else {
        setError(response.message || '인증 코드가 유효하지 않습니다.');
      }
    } catch (err) {
      setError('인증에 실패했습니다. 다시 시도해주세요.');
      console.error('Token verification error:', err);
    }
  };
  
  // 최종 회원가입
  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    
    if (password !== confirmPassword) {
      setError('비밀번호가 일치하지 않습니다.');
      return;
    }
    
    try {
      await register({
        email,
        password,
        name
      });
      
      navigate('/');
    } catch (err) {
      setError('회원가입에 실패했습니다. 다시 시도해주세요.');
      console.error('Registration error:', err);
    }
  };

  // 다음 단계로 이동하는 함수
  const renderCurrentStep = () => {
    switch (currentStep) {
      case SignupStep.EMAIL_INPUT:
        return (
          <form 
            className="auth-form" 
            onSubmit={handleSendVerificationEmail}
            method="post"
          >
            <div className="form-group">
              <label htmlFor="email">이메일</label>
              <input
                type="email"
                id="email"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            
            <button 
              type="submit"
              className="auth-submit-button"
              disabled={loading}
            >
              {loading ? '처리 중...' : '인증 메일 보내기'}
            </button>
          </form>
        );
        
      case SignupStep.TOKEN_VERIFICATION:
        return (
          <form 
            className="auth-form" 
            onSubmit={handleVerifyToken}
            method="post"
          >
            <p className="info-text">
              {email}로 인증 코드를 보냈습니다. 이메일을 확인하고 코드를 입력해주세요.
            </p>
            
            <div className="form-group">
              <label htmlFor="verificationToken">인증 코드</label>
              <input
                type="text"
                id="verificationToken"
                name="token"
                value={verificationToken}
                onChange={(e) => setVerificationToken(e.target.value)}
                required
              />
            </div>
            
            <button 
              type="submit"
              className="auth-submit-button"
              disabled={loading}
            >
              {loading ? '처리 중...' : '인증하기'}
            </button>
            
            <button 
              type="button"
              className="auth-back-button"
              onClick={() => setCurrentStep(SignupStep.EMAIL_INPUT)}
              disabled={loading}
            >
              이전으로
            </button>
          </form>
        );
        
      case SignupStep.USER_REGISTRATION:
        return (
          <form className="auth-form" onSubmit={handleRegister}>
            <p className="success-text">
              이메일 인증이 완료되었습니다. 회원 정보를 입력해주세요.
            </p>
            
            <div className="form-group">
              <label htmlFor="name">이름</label>
              <input
                type="text"
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            
            <div className="form-group">
              <label htmlFor="password">비밀번호</label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                minLength={6}
              />
            </div>
            
            <div className="form-group">
              <label htmlFor="confirmPassword">비밀번호 확인</label>
              <input
                type="password"
                id="confirmPassword"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                minLength={6}
              />
            </div>
            
            <button 
              type="submit"
              className="auth-submit-button"
              disabled={loading}
            >
              {loading ? '처리 중...' : '회원가입 완료'}
            </button>
            
            <button 
              type="button"
              className="auth-back-button"
              onClick={() => setCurrentStep(SignupStep.TOKEN_VERIFICATION)}
              disabled={loading}
            >
              이전으로
            </button>
          </form>
        );
        
      default:
        return null;
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <h1>회원가입</h1>
        
        {error && <div className="auth-error">{error}</div>}
        
        {renderCurrentStep()}
        
        <div className="auth-footer">
          <p>
            이미 계정이 있으신가요? <Link to="/login">로그인</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Signup;