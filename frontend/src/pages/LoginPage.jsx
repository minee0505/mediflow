import { useNavigate } from 'react-router-dom';
import googleLogo from '../assets/google-logo.svg';
import kakaoLogo from '../assets/kakao-logo.svg';
import mediflowIcon from '../assets/mediflow-icon.svg';

export default function LoginPage() {
    const navigate = useNavigate();

    const handleGoogleLogin = () => {
        window.location.href = `${import.meta.env.VITE_API_BASE_URL}/oauth2/authorization/google`;
    };

    const handleKakaoLogin = () => {
        window.location.href = `${import.meta.env.VITE_API_BASE_URL}/oauth2/authorization/kakao`;
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-sky-50 flex items-center justify-center px-4">
            <div className="w-full max-w-md">
                {/* 로고 섹션 */}
                <div className="text-center mb-12">
                    <div className="flex justify-center mb-6">
                        <img src={mediflowIcon} alt="MediFlow" className="w-24 h-24" />
                    </div>
                    <h1 className="text-4xl font-bold text-sky-700 mb-2">
                        MediFlow
                    </h1>
                    <p className="text-sky-600 text-sm">전자의무기록 시스템</p>
                </div>

                {/* 로그인 카드 */}
                <div className="bg-white rounded-2xl shadow-lg p-8">
                    {/* 버튼 그룹 */}
                    <div className="space-y-4">
                        {/* 로그인 버튼 */}
                        <button
                            onClick={() => navigate('/login')}
                            className="w-full py-3 px-4 bg-sky-500 hover:bg-sky-600 text-white rounded-lg font-medium transition-colors duration-200 shadow-md"
                        >
                            이메일로 로그인
                        </button>

                        {/* 회원가입 버튼 */}
                        <button
                            onClick={() => navigate('/register')}
                            className="w-full py-3 px-4 bg-white hover:bg-sky-50 text-sky-600 border-2 border-sky-500 rounded-lg font-medium transition-colors duration-200"
                        >
                            이메일로 회원가입
                        </button>

                        {/* 구분선 */}
                        <div className="relative my-8">
                            <div className="absolute inset-0 flex items-center">
                                <div className="w-full border-t border-gray-200"></div>
                            </div>
                            <div className="relative flex justify-center text-sm">
                                <span className="px-4 bg-white text-gray-500">또는</span>
                            </div>
                        </div>

                        {/* 소셜 로그인 버튼 */}
                        <button
                            onClick={handleGoogleLogin}
                            className="w-full py-3 px-4 bg-white hover:bg-gray-50 text-gray-700 border border-gray-300 rounded-lg font-medium transition-colors duration-200 flex items-center justify-center gap-3"
                        >
                            <img src={googleLogo} alt="Google" className="w-5 h-5" />
                            Google로 계속하기
                        </button>

                        <button
                            onClick={handleKakaoLogin}
                            className="w-full py-3 px-4 bg-[#FEE500] hover:bg-[#FDD835] text-gray-900 rounded-lg font-medium transition-colors duration-200 flex items-center justify-center gap-3"
                        >
                            <img src={kakaoLogo} alt="Kakao" className="w-5 h-5" />
                            카카오로 계속하기
                        </button>
                    </div>
                </div>

                {/* 하단 텍스트 */}
                <p className="text-center text-sm text-gray-500 mt-8">
                    로그인하시면{' '}
                    <a href="#" className="text-sky-600 hover:text-sky-700 font-medium">
                        이용약관
                    </a>
                    과{' '}
                    <a href="#" className="text-sky-600 hover:text-sky-700 font-medium">
                        개인정보처리방침
                    </a>
                    에 동의하는 것으로 간주됩니다.
                </p>
            </div>
        </div>
    );
}
